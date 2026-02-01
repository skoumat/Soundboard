package cz.utb.fai.soundboard.database

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import cz.utb.fai.soundboard.domainModels.MovieModel
import cz.utb.fai.soundboard.mappers.toDomainModel
import cz.utb.fai.soundboard.mappers.toEntityModel
import cz.utb.fai.soundboard.mappers.parse

class SoundboardRepository (
    private val movieDao: MoviesDao,
    private val soundDao: SoundsDao,
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

    suspend fun addMovie(movie: MovieModel){
        movieDao.addMovie(movie.toEntityModel())
    }
    suspend fun updateMovie(movie: MovieModel){
        movieDao.updateMovie(movie.toEntityModel())
    }
    suspend fun deleteMovie(movieId: Long){
        movieDao.deleteMovie(getMovie(movieId).toEntityModel())
    }


//    suspend fun getMovieInfo(): String? {
//        try {
//            // 1. Try to fetch from Network
//            val networkModel = apiService.getSubjectInfo(katedra, zkratka)
//
//            if (networkModel != null) {
//                val domainModel = networkModel.asDomainModel()
//
//                // 2. Save/Cache into Database
//                // Ensure you have creating the mapping function: SubjectInfoDomain.asEntityModel()
//                movieDao.insertMovie(domainModel.asEntityModel())
//
//                Log.d("Repository", "Data loaded from API and cached.")
//                return domainModel
//            }
//        } catch (e: Exception) {
//            // Log the error and return null to signal failure
//            Log.e("Repository", "API call failed", e)
//
//        }
//        // 3. Fallback: Try to fetch from Database (Cache)
//        try {
//            val entity = dao.selectByShortcut(zkratka)
//            if (entity != null) {
//                Log.d("Repository", "Data loaded from Database cache.")
//                return entity.asDomainModel()
//            }
//        } catch (e: Exception) {
//            Log.e("Repository", "Database fallback failed", e)
//        }
//
//        return null
//    }
}