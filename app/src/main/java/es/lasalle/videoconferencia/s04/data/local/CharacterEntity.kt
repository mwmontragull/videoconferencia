package es.lasalle.videoconferencia.s04.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    val id: Int,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "status")
    val status: String,
    
    @ColumnInfo(name = "species")
    val species: String,
    
    @ColumnInfo(name = "type")
    val type: String?,
    
    @ColumnInfo(name = "gender")
    val gender: String,
    
    @ColumnInfo(name = "origin_name")
    val originName: String,
    
    @ColumnInfo(name = "origin_url")
    val originUrl: String,
    
    @ColumnInfo(name = "location_name")
    val locationName: String,
    
    @ColumnInfo(name = "location_url")
    val locationUrl: String,
    
    @ColumnInfo(name = "image")
    val image: String,
    
    @ColumnInfo(name = "episode")
    val episode: List<String>,
    
    @ColumnInfo(name = "url")
    val url: String,
    
    @ColumnInfo(name = "created")
    val created: String,
    
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "last_accessed")
    val lastAccessed: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
) {
    val episodeCount: Int
        get() = episode.size
    
    fun isStale(maxAgeHours: Int = 24): Boolean {
        val maxAgeMillis = maxAgeHours * 60 * 60 * 1000L
        return (System.currentTimeMillis() - lastUpdated) > maxAgeMillis
    }
    
    fun withUpdatedTimestamp(): CharacterEntity {
        return this.copy(
            lastAccessed = System.currentTimeMillis()
        )
    }
    
    fun toggleFavorite(): CharacterEntity {
        return this.copy(
            isFavorite = !isFavorite,
            lastAccessed = System.currentTimeMillis()
        )
    }
}

object CharacterEntitySamples {
    val rickSanchez = CharacterEntity(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        originName = "Earth (C-137)",
        originUrl = "https://rickandmortyapi.com/api/location/1",
        locationName = "Earth (Replacement Dimension)",
        locationUrl = "https://rickandmortyapi.com/api/location/20",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        episode = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2",
            "https://rickandmortyapi.com/api/episode/3"
        ),
        url = "https://rickandmortyapi.com/api/character/1",
        created = "2017-11-04T18:48:46.250Z",
        isFavorite = true
    )
    
    val mortySmith = CharacterEntity(
        id = 2,
        name = "Morty Smith",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        originName = "Earth (C-137)",
        originUrl = "https://rickandmortyapi.com/api/location/1",
        locationName = "Earth (Replacement Dimension)",
        locationUrl = "https://rickandmortyapi.com/api/location/20",
        image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
        episode = listOf(
            "https://rickandmortyapi.com/api/episode/1",
            "https://rickandmortyapi.com/api/episode/2"
        ),
        url = "https://rickandmortyapi.com/api/character/2",
        created = "2017-11-04T18:50:21.651Z",
        isFavorite = false
    )
    
    val sampleList = listOf(rickSanchez, mortySmith)
}