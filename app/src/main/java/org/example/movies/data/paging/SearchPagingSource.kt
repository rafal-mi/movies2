package org.example.movies.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.example.movies.App.Companion.TAG
import org.example.movies.data.db.Movie
import org.example.movies.data.net.Api

private const val STARTING_PAGE_INDEX = 1

class SearchPagingSource(
    private val api: Api,
    private val query: String?
    ) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        val key = state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

        Log.d(TAG, "Refresh key is $key")

        return key
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val movies: List<Movie> = if(query != null) {
                val response = api.search(query, position)
                response.results
            } else {
                val response = api.getNowPlaying(position)
                response.results
            }

            LoadResult.Page(
                data = movies,
                prevKey = if(position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if(movies.isEmpty()) null else position + 1
            )

        } catch(e: Exception) {
            LoadResult.Error(e)
        }
    }
}