package org.example.movies.kotlin

import android.view.View
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow

fun SearchView.getQueryTextChangeStateFlow(): StateFlow<Pair<String, Boolean>> {

    val query = MutableStateFlow(Pair("", false))

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query_: String?): Boolean {
            query_?.apply {
                query.value = Pair(query_, false)
            }
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = Pair(newText, true)
            return true
        }
    })

    return query

}