package cz.utb.fai.soundboard.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

import cz.utb.fai.soundboard.database.entities.MovieEntity

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // nepotrebuju really, ale sure Using REPLACE strategy so if we fetch updated info for the same subject, it updates the DB
    suspend fun addMovie(movie: MovieEntity)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies")
    fun getAllMoviesFlow(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id == :id LIMIT 1")
    suspend fun getMovie(id: Long): MovieEntity

    @Query("SELECT charactersJson FROM movies WHERE id == :id LIMIT 1")
    suspend fun getMovieCharacters(id: Long): String?

    // TODO: do I even need seraching via text and filtering

    // Select specific subject by shortcut and department (or just shortcut if unique)
//    @Query("SELECT * FROM subject_info WHERE shortcut = :shortcut LIMIT 1")
//    suspend fun selectByShortcut(shortcut: String): SubjectInfoEntity?


}