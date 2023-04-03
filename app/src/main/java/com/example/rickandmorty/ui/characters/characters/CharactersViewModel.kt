package com.example.rickandmorty.ui.characters.characters

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.rickandmorty.data.datastore.CharacterStatus
import com.example.rickandmorty.data.datastore.PreferencesManager
import com.example.rickandmorty.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        delay(500L)
        repository.getSearchResults(query, filterPreferences.characterStatus)
            .cachedIn(viewModelScope)
    }

    val characters = charactersFlow.asLiveData()

    fun onCharacterStatusUpdate(characterStatus: CharacterStatus) = viewModelScope.launch {
        preferencesManager.updateCharacterStatus(characterStatus)
    }

    fun onAliveToggle(status: Boolean) = viewModelScope.launch {
        preferencesManager.updateStatusAlive(status)
    }

    fun onDeadToggle(status: Boolean) = viewModelScope.launch {
        preferencesManager.updateStatusDead(status)
    }
}
