package org.example.movies.data.repo

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.*
import org.example.movies.App
import org.example.movies.data.Result
import org.example.movies.data.db.Movie
import org.example.movies.data.net.Api
import org.example.movies.data.paging.MoviesPagingSource

class RepositoryDefault(
    private val api: Api
) : Repository {
    override fun pagedFlow(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 50,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviesPagingSource(App.instance.api, query) }
        ).flow

    override val queryFlow = MutableStateFlow("")

    override val searchFlow: Flow<Result<List<Movie>>> = queryFlow
        .map {
            Log.d(App.TAG, "Search flow value 1 is $it")
            it
        }
        .debounce(1000)
        .filter {
            it.isNotEmpty()
        }
        .map {
            Log.d(App.TAG, "Search flow value 2 is $it")
            it
        }
        .distinctUntilChanged()
        .mapLatest {
            Log.d(App.TAG, "API call")
            val r = api.search(it)
            //initFlow.value = false
            r
        }


}