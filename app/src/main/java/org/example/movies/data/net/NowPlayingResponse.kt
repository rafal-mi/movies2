package org.example.movies.data.net

import org.example.movies.data.db.Movie

data class NowPlayingResponse(
    val results: List<Movie>
)