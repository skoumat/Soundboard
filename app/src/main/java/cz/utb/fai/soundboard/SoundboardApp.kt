package cz.utb.fai.soundboard

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import cz.utb.fai.soundboard.database.SoundboardDatabase
import cz.utb.fai.soundboard.database.SoundboardRepository
import cz.utb.fai.soundboard.services.api.WikidataApiService

class SoundboardApp : Application() {
    val database: SoundboardDatabase by lazy {
        SoundboardDatabase.getDatabase(this)
    }

    val apiService: WikidataApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://query.wikidata.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(WikidataApiService::class.java)
    }

    val repository: SoundboardRepository by lazy {
        SoundboardRepository(database.moviesDao(), database.soundsDao(), apiService)
    }
}