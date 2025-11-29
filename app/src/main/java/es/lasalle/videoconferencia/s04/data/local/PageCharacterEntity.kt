package es.lasalle.videoconferencia.s04.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "page_characters",
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("characterId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["page"]),
        Index(value = ["characterId"]),
        Index(value = ["page", "position"])
    ]
)
data class PageCharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "page")
    val page: Int,
    
    @ColumnInfo(name = "characterId")
    val characterId: Int,
    
    @ColumnInfo(name = "position")
    val position: Int,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "last_fetched")
    val lastFetched: Long = System.currentTimeMillis()
) {
    fun isStale(maxAgeHours: Int = 24): Boolean {
        val maxAgeMillis = maxAgeHours * 60 * 60 * 1000L
        return (System.currentTimeMillis() - lastFetched) > maxAgeMillis
    }
    
    fun withUpdatedTimestamp(): PageCharacterEntity {
        return this.copy(
            lastFetched = System.currentTimeMillis()
        )
    }
}

@Entity(
    tableName = "page_metadata",
    indices = [Index(value = ["page"], unique = true)]
)
data class PageMetadataEntity(
    @PrimaryKey
    val page: Int,
    
    @ColumnInfo(name = "total_count")
    val totalCount: Int,
    
    @ColumnInfo(name = "total_pages")
    val totalPages: Int,
    
    @ColumnInfo(name = "next_page_url")
    val nextPageUrl: String?,
    
    @ColumnInfo(name = "previous_page_url")
    val previousPageUrl: String?,
    
    @ColumnInfo(name = "fetched_at")
    val fetchedAt: Long = System.currentTimeMillis()
) {
    val hasNextPage: Boolean
        get() = nextPageUrl != null
    
    val hasPreviousPage: Boolean
        get() = previousPageUrl != null
    
    fun isStale(maxAgeHours: Int = 168): Boolean {
        val maxAgeMillis = maxAgeHours * 60 * 60 * 1000L
        return (System.currentTimeMillis() - fetchedAt) > maxAgeMillis
    }
}

object PageEntitySamples {
    val page1Characters = listOf(
        PageCharacterEntity(
            page = 1,
            characterId = 1,
            position = 0
        ),
        PageCharacterEntity(
            page = 1,
            characterId = 2,
            position = 1
        )
    )
    
    val page1Metadata = PageMetadataEntity(
        page = 1,
        totalCount = 826,
        totalPages = 42,
        nextPageUrl = "https://rickandmortyapi.com/api/character/?page=2",
        previousPageUrl = null
    )
    
    val page2Metadata = PageMetadataEntity(
        page = 2,
        totalCount = 826,
        totalPages = 42,
        nextPageUrl = "https://rickandmortyapi.com/api/character/?page=3",
        previousPageUrl = "https://rickandmortyapi.com/api/character/?page=1"
    )
}