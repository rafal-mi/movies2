package org.example.movies

import org.example.movies.data.net.Api
import org.example.movies.data.net.ApiImpl

object Locator {

    @Volatile
    var api: Api? = null

    fun provideApi(): Api {
        synchronized(this ) {
            return api ?: ApiImpl()
        }
    }
}