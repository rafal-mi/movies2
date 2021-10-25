package org.example.movies

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.movies.data.db.Movie
import org.example.movies.data.repo.Repository
import java.util.*

class MainViewModel(
    private val repository: Repository,
    private val state: SavedStateHandle
) : ViewModel() {
    private lateinit var message: String

    private val _listPositionEvent = MutableLiveData<Event<Int>>()
    val listPositionEvent: LiveData<Event<Int>> = _listPositionEvent

    private val _changeListEvent = MutableLiveData<Event<Long>>()
    val changeListEvent: LiveData<Event<Long>> = _changeListEvent

    val queryTextFlow = MutableStateFlow("")

    val moviesLiveData = repository.moviesFlow.asLiveData()

    val autocompleteLiveData = repository.autocompleteFlow
        .asLiveData()

    fun setQuery(query: String?) {
        repository.queryFlow.value = query
//        viewModelScope.launch {
//            delay(200)
//            _changeListEvent.value = Event(Date().time)
//        }
    }

    fun setAutocompleteQuery(query: String) {
        repository.autocompleteQueryFlow.value = query
    }

    fun mark(movie: Movie, position: Int) {
        message = "Marking item at position $position"
        Log.i(App.TAG, message)

        Date().time

    }

    init {
        viewModelScope.launch {
            queryTextFlow
                .collect {
                    repository.autocompleteQueryFlow.value = it
                }
        }
    }
}

class MainViewModelFactory(
    private val repository: Repository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle?
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) =
        MainViewModel(repository, handle) as T

}