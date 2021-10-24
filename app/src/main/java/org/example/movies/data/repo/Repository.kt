package org.example.movies.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.example.movies.App
import org.example.movies.data.Result
import org.example.movies.data.db.Movie
import org.example.movies.data.paging.MoviesPagingSource

interface Repository {
    fun pagedFlow(query: String) : Flow<PagingData<Movie>>

    val queryFlow: MutableStateFlow<String>

    val searchFlow: Flow<Result<List<Movie>>>

}