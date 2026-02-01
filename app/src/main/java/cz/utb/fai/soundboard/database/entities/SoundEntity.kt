package cz.utb.fai.soundboard.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sounds")
data class SoundEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val charactersJson: String,
    val movieId: Long,
    val filePathString: String
)
