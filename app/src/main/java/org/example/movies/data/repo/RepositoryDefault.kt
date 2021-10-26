package org.example.movies.data.repo

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.*
import org.example.movies.App
import org.example.movies.App.Companion.TAG
import org.example.movies.data.db.Movie
import org.example.movies.data.db.MovieDao
import org.example.movies.data.net.Api
import org.example.movies.data.paging.MoviesPagingSource
import org.example.movies.data.paging.SearchPagingSource

class RepositoryDefault(
    private val api: Api,
    private val dao: MovieDao
) : Repository {
    override fun nowPlayingFLow(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { MoviesPagingSource(App.instance.api, query) }
        ).flow

    private fun queriedFlow(query: String?) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { SearchPagingSource(App.instance.api, query) }
        ).flow

    override val queryFlow = MutableStateFlow<String?>(null)
    override val moviesFlow = queryFlow
        .map {
            Log.d(TAG, "Query flow value is $it")

            it
        }
        .flatMapLatest {
            val r = queriedFlow(query = it)

            Log.d(TAG, "Queried page flow value is $r")

            r
        }
        .map {
            Log.d(TAG, "Queried paging data are $it")
            it
        }

    override val autocompleteQueryFlow = MutableStateFlow("")
    override val autocompleteFlow: Flow<List<Movie>> = autocompleteQueryFlow
        .map {
            Log.d(App.TAG, "Autocomplete flow value 1 is $it")
            it
        }
        .debounce(2000)
        .filter {
            it.isNotEmpty()
        }
        .map {
            Log.d(App.TAG, "Autocomplete flow value 2 is $it")
            it
        }
        .distinctUntilChanged()
        .mapLatest {
            Log.d(App.TAG, "API call from autocomplete flow")
            val r = api.search(it, 1)

            r.results
        }

    override suspend fun save(movie: Movie) {
        dao.insert(movie)
    }

    override val listFlow: Flow<List<Movie>> =
        dao.listOfFavoritesFlow()


}