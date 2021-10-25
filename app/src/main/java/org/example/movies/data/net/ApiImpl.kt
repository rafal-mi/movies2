package org.example.movies.data.net

import android.net.Uri
import android.util.Log
import com.google.gson.FieldNamingPolicy
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import org.example.movies.App
import org.example.movies.data.db.Movie
import java.lang.Exception
import org.example.movies.data.Result

class ApiImpl: Api {
    private lateinit var message: String

    override suspend fun getNowPlaying(): NowPlayingResponse {
        val url = "$API_BASE_URL/movie/now_playing?api_key=${App.instance.api_key}"

        message = "URL is $url"
        Log.i(TAG, message)

        val client = HttpClient(Android) {
            install(DefaultRequest) {
                headers.append("Accept", "application/json")
            }

            install(JsonFeature) {
                serializer = GsonSerializer { setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) }
            }
        }

        return try {
            var r: NowPlayingResponse = client.get(url)

            message = "GET $url resolved with $r"
            Log.i(TAG, message)

            r
        } catch (e: Exception) {
            message = "GET $url rejected with $e"
            Log.e(TAG, message)

            throw e
        }
    }

    override suspend fun search(query: String): SearchResponse {
        val encoded = //URLEncoder.encode(query, "utf-8")
            Uri.encode(query)
        val url = "$API_BASE_URL/search/movie?query=$encoded&api_key=${App.instance.api_key}"

        message = "URL is $url"
        Log.i(TAG, message)

        val client = HttpClient(Android) {
            install(DefaultRequest) {
                headers.append("Accept", "application/json")
            }

            install(JsonFeature) {
                serializer = GsonSerializer { setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) }
            }
        }

        return try {
            var r: SearchResponse = client.get(url)

            message = "GET $url resolved with $r"
            Log.i(TAG, message)

            r
        } catch (e: Exception) {
            message = "GET $url rejected with $e"
            Log.e(TAG, message)

            throw e
        }
    }

    companion object {
        const val API_BASE_URL = "https://api.themoviedb.org/3"
        const val TAG = App.TAG
    }
}