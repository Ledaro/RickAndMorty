package com.example.rickandmorty.ui.characters.characters

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.rickandmorty.data.datastore.PreferencesManager
import com.example.rickandmorty.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: Repository,
    private val preferencesManager: PreferencesManager,
    state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("search_query", "")
    val preferencesFlow = preferencesManager.preferencesFlow

    @OptIn(ExperimentalCoroutinesApi::class)
    private val charactersFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow.distinctUntilChanged(),
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        Log.d("API_CALL", "Making API call for query: $query")
        delay(500L)
        repository.getSearchResults(query, filterPreferences.isAlive, filterPreferences.isDead)
            .cachedIn(viewModelScope)
    }

    val characters = charactersFlow.asLiveData()

    fun onAliveToggle(status: Boolean) = viewModelScope.launch {
        preferencesManager.updateIsAlive(status)
    }

    fun onDeadToggle(status: Boolean) = viewModelScope.launch {
        preferencesManager.updateIsDead(status)
    }
}
