package cz.utb.fai.soundboard.database

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

import cz.utb.fai.soundboard.domainModels.MovieModel
import cz.utb.fai.soundboard.domainModels.SoundModel
import cz.utb.fai.soundboard.mappers.toDomainModel
import cz.utb.fai.soundboard.mappers.toEntityModel
import cz.utb.fai.soundboard.mappers.parse

import cz.utb.fai.soundboard.services.api.MovieDetailsModel
import cz.utb.fai.soundboard.services.api.WikidataApiService
import cz.utb.fai.soundboard.services.api.WikidataResponse

class SoundboardRepository (
    private val movieDao: MoviesDao,
    private val soundDao: SoundsDao,
    private val apiService: WikidataApiService
) {
    fun getAllMoviesFlow(): Flow<List<MovieModel>> {
        return movieDao.getAllMoviesFlow().map { entities ->
            entities.map { entity ->
                MovieModel(entity.id, entity.name, parse(entity.charactersJson).toMutableList())
            }
        }
    }

    suspend fun getMovie(id: Long) : MovieModel{
        return movieDao.getMovie(id).toDomainModel()
    }

    fun getMovieCharacters(movieId: Long) : Flow<List<String>>  = flow{
        val characters = parse(
            movieDao.getMovieCharacters(movieId)
        )
            .toList()
            .distinct()

        emit(characters)
    }


    suspend fun addMovie(movie: MovieModel){
        movieDao.addMovie(movie.toEntityModel())
    }
    suspend fun updateMovie(movie: MovieModel){
        movieDao.updateMovie(movie.toEntityModel())
    }
    suspend fun deleteMovie(movieId: Long){
        movieDao.deleteMovie(getMovie(movieId).toEntityModel())
    }


    fun getAllSoundsFlow(movieId: Long): Flow<List<SoundModel>> {
        return soundDao.getAllSoundsFlow(movieId).map { entities ->
            entities.map { entity ->
                entity.toDomainModel()
            }
        }
    }

    suspend fun getSound(id: Long) : SoundModel{
        return soundDao.getSound(id).toDomainModel()
    }

    suspend fun addSound(sound: SoundModel){
        soundDao.addSound(sound.toEntityModel())
    }
    suspend fun updateSound(sound: SoundModel){
        soundDao.updateSound(sound.toEntityModel())
    }
    suspend fun deleteSound(soundId: Long){
        soundDao.deleteSound(getSound(soundId).toEntityModel())
    }

    fun getSoundCharacters(soundId : Long) : Flow<List<String>> = flow{
        val soundChars = parse(soundDao.getSoundCharacters(soundId)).toList().distinct()

        emit(soundChars)
    }


    suspend fun fetchMoviesFromWiki(name: String): List<MovieDetailsModel> {
        val sparqlQuery = """
        SELECT ?movie ?movieLabel ?directorLabel ?releaseDate ?actorLabel WHERE {
          ?movie wdt:P31 wd:Q11424;        # instance of film
                 rdfs:label "$name"@en.  # exact match by name
          OPTIONAL { ?movie wdt:P57 ?director. }
          OPTIONAL { ?movie wdt:P161 ?actor. }
          OPTIONAL { ?movie wdt:P577 ?releaseDate. }
          SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }
        }
    """.trimIndent()

        var response : WikidataResponse? = null
        Log.e("fetchMoviesFromWiki", "before response")
        try{
            response = apiService.getMovies(sparqlQuery)
        } catch (e: Exception) {
            Log.e("WIKIDATA", e.toString())
        }

        Log.e("fetchMoviesFromWiki", "after response")

        val moviesMap = mutableMapOf<String, MovieDetailsModel>()

        if (response == null)
            return emptyList()

        response.results.bindings.forEach { binding ->
            val title = binding.movieLabel?.value ?: return@forEach
            val director = binding.directorLabel?.value
            val releaseDate = binding.releaseDate?.value
            val actor = binding.actorLabel?.value

            val existing = moviesMap[title]
            if (existing != null) {
                val updatedCast = existing.cast.toMutableSet()
                if (actor != null) updatedCast.add(actor)
                moviesMap[title] = existing.copy(cast = updatedCast.toList())
            } else {
                moviesMap[title] = MovieDetailsModel(
                    title = title,
                    director = director,
                    releaseDate = releaseDate,
                    cast = if (actor != null) listOf(actor) else emptyList()
                )
            }
        }

        Log.e("fetchMoviesFromWiki", "after mapping")

        return moviesMap.values.toList()
    }
}