package cz.utb.fai.soundboard.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import cz.utb.fai.soundboard.database.entities.MovieEntity
import cz.utb.fai.soundboard.database.entities.SoundEntity

@Database(entities = [MovieEntity::class, SoundEntity::class], version = 1, exportSchema = false)
abstract class SoundboardDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
    abstract fun soundsDao(): SoundsDao

    companion object {
        @Volatile
        private var INSTANCE: SoundboardDatabase? = null

        fun getDatabase(context: Context): SoundboardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SoundboardDatabase::class.java,
                    "soundboard_database"
                )
                    .fallbackToDestructiveMigration(true) // Wipes and rebuilds instead of migrating if no Migration object. Useful for development, but remove for production.
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}