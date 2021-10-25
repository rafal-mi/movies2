package org.example.movies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.example.movies.data.db.Movie
import org.example.movies.data.net.Api

private const val STARTING_PAGE_INDEX = 1

class MoviesPagingSource(
    private val api: Api,
    private val query: String
    ) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = api.getNowPlaying(position)
            val movies = response.results

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