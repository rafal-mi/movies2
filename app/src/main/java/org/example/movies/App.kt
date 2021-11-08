package org.example.movies

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import org.example.movies.data.db.AppDatabase
import org.example.movies.data.net.Api
import org.example.movies.data.net.ApiImpl
import org.example.movies.data.repo.Repository
import org.example.movies.data.repo.RepositoryDefault

class App: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val api: Api by lazy { ApiImpl() }
    @ExperimentalPagingApi
    val repository: Repository by lazy { RepositoryDefault(api, database.movieDao(), database) }
    val api_key: String by lazy { getString(R.string.api_key) }


    init {
        instance = this
    }

    companion object {
        lateinit var instance: App
        const val TAG = "Movies"
    }

}