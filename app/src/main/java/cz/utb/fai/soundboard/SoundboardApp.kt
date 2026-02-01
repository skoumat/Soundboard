package cz.utb.fai.soundboard

import android.app.Application
import cz.utb.fai.soundboard.database.SoundboardDatabase
import cz.utb.fai.soundboard.database.SoundboardRepository

//@HiltAndroidApp
class SoundboardApp : Application() {
    val database: SoundboardDatabase by lazy {
        SoundboardDatabase.getDatabase(this)
    }
    val repository: SoundboardRepository by lazy {
        SoundboardRepository(database.moviesDao(), database.soundsDao())
    }
}