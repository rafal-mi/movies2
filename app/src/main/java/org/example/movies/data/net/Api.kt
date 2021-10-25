package org.example.movies.data.net

import org.example.movies.data.db.Movie
import org.example.movies.data.Result

interface Api {

    suspend fun getNowPlaying(page: Int): NowPlayingResponse
    suspend fun search(query: String, page: Int): SearchResponse
}