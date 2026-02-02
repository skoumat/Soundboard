package cz.utb.fai.soundboard.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

import cz.utb.fai.soundboard.database.entities.SoundEntity

@Dao
interface SoundsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSound(sound: SoundEntity): Long

    @Update
    suspend fun updateSound(sound: SoundEntity)

    @Delete
    suspend fun deleteSound(sound: SoundEntity)

    @Query("SELECT * FROM sounds WHERE movieId == :movieId")
    fun getAllSoundsFlow(movieId: Long):  Flow<List<SoundEntity>>

    @Query("SELECT * FROM sounds WHERE id == :id LIMIT 1")
    suspend fun getSound(id: Long): SoundEntity

    @Query("SELECT charactersJson FROM sounds WHERE id == :soundId LIMIT 1")
    suspend fun getSoundCharacters(soundId:Long): String?

}