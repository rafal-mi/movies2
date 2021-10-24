package org.example.movies

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import kotlinx.coroutines.flow.*
import org.example.movies.data.Result
import org.example.movies.data.repo.Repository

class MainViewModel(
    private val repository: Repository,
    private val state: SavedStateHandle
) : ViewModel() {
    private lateinit var message: String

    private val _listPositionEvent = MutableLiveData<Event<Int>>()
    val listPositionEvent: LiveData<Event<Int>> = _listPositionEvent

    lateinit var queryTextChangeStateFlow: StateFlow<String>
    lateinit var isQueryTypedNowFlow: Flow<Boolean>
    lateinit var isQueryTypedNowLiveData: LiveData<Boolean>

    val queryTextChangeFlow = MutableStateFlow<String>("")
    val isQueryTypedFlow = queryTextChangeFlow
        .debounce(1000)
        .map {
            it.isNotEmpty()
        }
    val isQueryTypedLiveData = isQueryTypedFlow.asLiveData()

    val searchLiveData = repository.searchFlow
        .filter {
            it is Result.Success
        }
        .map {
            (it as Result.Success).data
        }
        .asLiveData()

    fun setQuery(query: String) {
        repository.queryFlow.value = query
    }

    init {
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