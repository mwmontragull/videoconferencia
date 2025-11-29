package es.lasalle.videoconferencia.s04.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterByIdFlow(id: Int): Flow<CharacterEntity?>

    @Query("SELECT * FROM characters ORDER BY id ASC")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters ORDER BY id ASC")
    fun getAllCharactersFlow(): Flow<List<CharacterEntity>>

    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCharacterCount(): Int

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :name || '%' COLLATE NOCASE ORDER BY name ASC")
    suspend fun searchCharactersByName(name: String): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE status = :status ORDER BY name ASC")
    suspend fun getCharactersByStatus(status: String): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE species = :species ORDER BY name ASC")
    suspend fun getCharactersBySpecies(species: String): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE is_favorite = 1 ORDER BY last_accessed DESC")
    fun getFavoriteCharactersFlow(): Flow<List<CharacterEntity>>

    @Query("""
        SELECT characters.* 
        FROM characters 
        INNER JOIN page_characters ON characters.id = page_characters.characterId 
        WHERE page_characters.page = :page 
        ORDER BY page_characters.position ASC
    """)
    suspend fun getCharactersByPage(page: Int): List<CharacterEntity>

    @Query("""
        SELECT characters.* 
        FROM characters 
        INNER JOIN page_characters ON characters.id = page_characters.characterId 
        WHERE page_characters.page = :page 
        ORDER BY page_characters.position ASC
    """)
    fun getCharactersByPageFlow(page: Int): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPagedCharacters(limit: Int, offset: Int): List<CharacterEntity>

    @Query("SELECT * FROM characters ORDER BY last_accessed DESC LIMIT :limit")
    suspend fun getRecentlyAccessedCharacters(limit: Int = 10): List<CharacterEntity>

    @Update
    suspend fun updateCharacter(character: CharacterEntity)

    @Query("UPDATE characters SET last_accessed = :lastAccessed WHERE id = :characterId")
    suspend fun updateLastAccessed(characterId: Int, lastAccessed: Long = System.currentTimeMillis())

    @Query("""
        UPDATE characters 
        SET is_favorite = :isFavorite, last_accessed = :timestamp 
        WHERE id = :characterId
    """)
    suspend fun updateFavoriteStatus(
        characterId: Int, 
        isFavorite: Boolean, 
        timestamp: Long = System.currentTimeMillis()
    )

    @Delete
    suspend fun deleteCharacter(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :characterId")
    suspend fun deleteCharacterById(characterId: Int)

    @Query("DELETE FROM characters")
    suspend fun deleteAllCharacters()

    @Query("DELETE FROM characters WHERE last_accessed < :olderThan")
    suspend fun deleteOldCharacters(olderThan: Long): Int

    @Query("DELETE FROM characters WHERE is_favorite = 0")
    suspend fun deleteNonFavoriteCharacters(): Int

    @Transaction
    suspend fun insertCharacterWithPageInfo(
        character: CharacterEntity,
        page: Int,
        position: Int
    ) {
        insertCharacter(character)
    }
}

@Dao
interface PageCharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageCharacter(pageCharacter: PageCharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageCharacters(pageCharacters: List<PageCharacterEntity>)

    @Query("SELECT * FROM page_characters WHERE page = :page ORDER BY position ASC")
    suspend fun getPageCharacters(page: Int): List<PageCharacterEntity>

    @Query("DELETE FROM page_characters WHERE page = :page")
    suspend fun deletePageCharacters(page: Int)

    @Query("DELETE FROM page_characters")
    suspend fun deleteAllPageCharacters()
}

@Dao
interface PageMetadataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageMetadata(metadata: PageMetadataEntity)

    @Query("SELECT * FROM page_metadata WHERE page = :page")
    suspend fun getPageMetadata(page: Int): PageMetadataEntity?

    @Query("DELETE FROM page_metadata WHERE page = :page")
    suspend fun deletePageMetadata(page: Int)

    @Query("DELETE FROM page_metadata")
    suspend fun deleteAllPageMetadata()
}