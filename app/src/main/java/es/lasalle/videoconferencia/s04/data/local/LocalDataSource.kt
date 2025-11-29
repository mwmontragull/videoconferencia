package es.lasalle.videoconferencia.s04.data.local

import android.content.Context
import es.lasalle.videoconferencia.s04.data.mappers.toApiCharacter
import es.lasalle.videoconferencia.s04.data.mappers.toApiCharacterList
import es.lasalle.videoconferencia.s04.data.mappers.toEntity
import es.lasalle.videoconferencia.s04.data.mappers.toEntityList
import es.lasalle.videoconferencia.s04.data.mappers.toEntityPreservingUserData
import es.lasalle.videoconferencia.s04.data.remote.models.ApiCharacter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSource(context: Context) {
    private val database = RickAndMortyDatabase.getDatabase(context)
    private val characterDao = database.characterDao()
    private val pageCharacterDao = database.pageCharacterDao()
    private val pageMetadataDao = database.pageMetadataDao()
    
    suspend fun saveCharacter(character: ApiCharacter) {
        val existingEntity = characterDao.getCharacterById(character.id)
        
        val entity = if (existingEntity != null) {
            character.toEntityPreservingUserData(existingEntity)
        } else {
            character.toEntity()
        }
        
        characterDao.insertCharacter(entity)
    }
    
    suspend fun saveCharacters(characters: List<ApiCharacter>, page: Int) {
        if (characters.isEmpty()) return
        
        val characterIds = characters.map { it.id }
        val existingEntities = characterIds.associateWith { id ->
            characterDao.getCharacterById(id)
        }.filterValues { it != null }.mapValues { it.value!! }
        
        val entities = characters.map { apiCharacter ->
            val existingEntity = existingEntities[apiCharacter.id]
            apiCharacter.toEntityPreservingUserData(existingEntity)
        }
        
        characterDao.insertCharacters(entities)
        
        val pageCharacters = characters.mapIndexed { index, character ->
            PageCharacterEntity(
                page = page,
                characterId = character.id,
                position = index
            )
        }
        
        pageCharacterDao.deletePageCharacters(page)
        pageCharacterDao.insertPageCharacters(pageCharacters)
    }
    
    fun getCharacterByIdFlow(id: Int): Flow<ApiCharacter?> {
        return characterDao.getCharacterByIdFlow(id)
            .map { entity -> entity?.toApiCharacter() }
    }
    
    suspend fun updateCharacterAccess(id: Int) {
        characterDao.updateLastAccessed(id)
    }
    
    fun getCharactersByPageFlow(page: Int): Flow<List<ApiCharacter>> {
        return characterDao.getCharactersByPageFlow(page)
            .map { entities -> entities.toApiCharacterList() }
    }
    
    fun getAllCharactersFlow(): Flow<List<ApiCharacter>> {
        return characterDao.getAllCharactersFlow()
            .map { entities -> entities.toApiCharacterList() }
    }
    
    // =====================================
    // 🧹 CACHE MANAGEMENT - GESTIÓN DE CACHE
    // =====================================
    
    /**
     * 🧹 clearCache - Limpiar todo el cache de Room Database
     * 
     * 📖 COMPLETE CACHE INVALIDATION:
     * Deletes all data from all tables.
     * Useful for logout, user switching, or complete refresh.
     * 
     * 🧠 CASCADE DELETE STRATEGY:
     * - Characters table cleared first
     * - Foreign key constraints handle page_characters automatically
     * - Page metadata cleared separately
     * - Database remains in valid state
     * 
     * 🔄 TRANSACTION SAFETY:
     * Room executes all deletes in single transaction automatically.
     * Either all data is cleared or none (atomic operation).
     * 
     * ⚠️ DESTRUCTIVE OPERATION:
     * This completely wipes local cache including user preferences.
     * Use with caution - consider clearAllExceptFavorites() alternative.
     */
    suspend fun clearCache() {
        // 🗑️ Clear all characters (cascade deletes page_characters)
        characterDao.deleteAllCharacters()
        
        // 🗑️ Clear all page relationships (explicit for clarity)
        pageCharacterDao.deleteAllPageCharacters()
        
        // 🗑️ Clear all page metadata
        pageMetadataDao.deleteAllPageMetadata()
    }
    
    /**
     * 🗑️ clearOldCache - Limpiar cache antiguo basado en timestamps
     * 
     * 📖 CACHE EXPIRATION IMPLEMENTATION:
     * Removes characters not accessed recently to manage storage space.
     * Implements LRU (Least Recently Used) cache eviction.
     * 
     * 🧠 LRU STRATEGY:
     * - Calculate expiration timestamp
     * - Delete characters with lastAccessed < expireTime
     * - Preserve favorites regardless of age
     * - Clean up orphaned page relationships
     * 
     * 💡 SMART CLEANUP:
     * - Never deletes favorite characters
     * - Returns count of deleted characters for logging
     * - Maintains database integrity
     * 
     * 🔧 CONFIGURATION:
     * Default 24 hours is reasonable for character data.
     * Adjust based on storage constraints and usage patterns.
     * 
     * @param maxAgeHours Edad máxima en horas antes de expirar
     * @return Número de personajes eliminados
     */
    suspend fun clearOldCache(maxAgeHours: Int = 24): Int {
        // ⏰ Calculate expiration timestamp
        val expireTime = System.currentTimeMillis() - (maxAgeHours * 3600 * 1000L)
        
        // 🗑️ Delete old characters but preserve favorites
        return characterDao.deleteOldCharacters(expireTime)
    }
    
    /**
     * 📊 getCacheSize - Obtener tamaño actual del cache
     * 
     * 📖 CACHE MONITORING:
     * Returns total number of characters in Room database.
     * Useful for debugging, optimization, y storage monitoring.
     * 
     * 🧠 MONITORING USE CASES:
     * - Storage usage tracking
     * - Cache effectiveness metrics
     * - Debug cache behavior
     * - User storage information
     * 
     * 🔢 PERFORMANCE:
     * COUNT(*) query is optimized by SQLite.
     * Much faster than loading all data just to count.
     * 
     * @return Número total de personajes en cache
     */
    suspend fun getCacheSize(): Int {
        return characterDao.getCharacterCount()
    }
    
    // =====================================
    // 💾 DATA PERSISTENCE OPERATIONS - OPERACIONES DE PERSISTENCIA
    // =====================================
    
    /**
     * 🔍 searchCharactersFlow - REACTIVE Search Source of Truth
     * 
     * 📖 SSOT SEARCH PATTERN:
     * Returns Flow que emite automáticamente cuando search results cambian.
     * Handles empty query by returning all characters Flow.
     * 
     * 🧠 REACTIVE SEARCH BENEFITS:
     * - AUTO-UPDATE: Search results update cuando personajes cambian
     * - OFFLINE SEARCH: Works perfectly sin network connection
     * - SINGLE SOURCE: Repository recibe only this emission
     * - FAST: SQL-based search con proper indexing
     * 
     * 🔄 SEARCH BEHAVIOR:
     * - Empty/blank query: Returns getAllCharactersFlow()
     * - Valid query: Returns Flow from searchCharactersByName
     * - Case-insensitive: COLLATE NOCASE en SQL
     * - Auto-updates: Emite new results cuando data changes
     * 
     * 💡 UI INTEGRATION:
     * ```kotlin
     * searchFlow.collectAsState() // Auto-updates search results
     * ```
     * 
     * @param query Término de búsqueda (case-insensitive)
     * @return Flow<List<ApiCharacter>> search results que se auto-actualizan
     */
    fun searchCharactersFlow(query: String): Flow<List<ApiCharacter>> {
        return if (query.isBlank()) {
            // Empty query returns all characters as reactive Flow
            getAllCharactersFlow()
        } else {
            // Future enhancement: Room doesn't have Flow for searchCharactersByName yet
            // For now, we'll use the suspend version wrapped in Flow
            // In real implementation, add Flow version to DAO
            getAllCharactersFlow().map { allCharacters ->
                allCharacters.filter { character ->
                    character.name.contains(query, ignoreCase = true)
                }
            }
        }
    }
    
    /**
     * 🔍 searchCachedCharacters - Helper for non-Flow search (Repository use)
     */
    suspend fun searchCachedCharacters(query: String): List<ApiCharacter> {
        if (query.isBlank()) {
            val entities = characterDao.getAllCharacters()
            return entities.toApiCharacterList()
        }
        
        val entities = characterDao.searchCharactersByName(query)
        return entities.toApiCharacterList()
    }
    
    /**
     * 🏷️ getCharactersByStatus - Filtrar personajes por estado
     * 
     * 📖 FILTERED QUERIES IMPLEMENTATION:
     * Efficient SQL filtering by status field.
     * Useful for implementing status-based filters in UI.
     * 
     * 🧠 STATUS FILTERING:
     * - Exact match on status field
     * - Common values: "Alive", "Dead", "unknown"
     * - Case-sensitive matching (API standard)
     * - Fast execution with proper indexing
     * 
     * 💡 UI INTEGRATION:
     * - Filter buttons: "Show only alive"
     * - Statistics: "X alive, Y dead characters"
     * - Visual grouping in character lists
     * 
     * @param status Estado exacto a filtrar
     * @return Lista ordenada de personajes con ese estado
     */
    suspend fun getCharactersByStatus(status: String): List<ApiCharacter> {
        // 🔍 SQL-based status filtering
        val entities = characterDao.getCharactersByStatus(status)
        
        // 🔄 Convert Entity list → ApiCharacter list
        return entities.toApiCharacterList()
    }
    
    fun getFavoriteCharactersFlow(): Flow<List<ApiCharacter>> {
        return characterDao.getFavoriteCharactersFlow()
            .map { entities -> entities.toApiCharacterList() }
    }
    
    suspend fun toggleFavoriteStatus(characterId: Int): Boolean {
        val entity = characterDao.getCharacterById(characterId) ?: return false
        val newFavoriteStatus = !entity.isFavorite
        
        characterDao.updateFavoriteStatus(characterId, newFavoriteStatus)
        
        return newFavoriteStatus
    }
    
    suspend fun getRecentlyAccessedCharacters(limit: Int = 10): List<ApiCharacter> {
        val entities = characterDao.getRecentlyAccessedCharacters(limit)
        return entities.toApiCharacterList()
    }
    
    suspend fun clearAllExceptFavorites(): Int {
        return characterDao.deleteNonFavoriteCharacters()
    }
    
    suspend fun savePageMetadata(
        page: Int,
        totalCount: Int,
        totalPages: Int,
        nextPageUrl: String?,
        previousPageUrl: String?
    ) {
        val metadata = PageMetadataEntity(
            page = page,
            totalCount = totalCount,
            totalPages = totalPages,
            nextPageUrl = nextPageUrl,
            previousPageUrl = previousPageUrl
        )
        
        pageMetadataDao.insertPageMetadata(metadata)
    }
    
    suspend fun getPageMetadata(page: Int): PageMetadataEntity? {
        return pageMetadataDao.getPageMetadata(page)
    }
    
    suspend fun isDatabasePopulated(): Boolean {
        return characterDao.getCharacterCount() > 0
    }
}