package cz.utb.fai.soundboard.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import cz.utb.fai.soundboard.database.entities.SoundEntity

@Dao
interface SoundsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSound(sound: SoundEntity)

    @Query("SELECT * FROM sounds")
    suspend fun getAllSounds(): List<SoundEntity>
}