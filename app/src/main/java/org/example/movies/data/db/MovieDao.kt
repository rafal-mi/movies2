package org.example.movies.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<Movie>)

    @Query("SELECT * FROM movie WHERE original_title LIKE :query ORDER BY release_date DESC")
    fun moviesByTitle(query: String): PagingSource<Int, Movie>

    @Query("DELETE FROM movie")
    suspend fun clear()
}