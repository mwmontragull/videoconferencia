package es.lasalle.videoconferencia.s04.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        CharacterEntity::class,
        PageCharacterEntity::class,
        PageMetadataEntity::class
    ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class RickAndMortyDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun pageCharacterDao(): PageCharacterDao
    abstract fun pageMetadataDao(): PageMetadataDao

    companion object {
        private const val DATABASE_NAME = "rick_and_morty_database"

        @Volatile
        private var INSTANCE: RickAndMortyDatabase? = null

        fun getDatabase(context: Context): RickAndMortyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    RickAndMortyDatabase::class.java,
                    DATABASE_NAME
                ).apply {
                    fallbackToDestructiveMigration()
                }.build()
                
                INSTANCE = instance
                instance
            }
        }
    }
}