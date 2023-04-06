package com.example.rickandmorty.ui.characters.characters

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.rickandmorty.data.datastore.PreferencesManager
import com.example.rickandmorty.data.repository.Repository
import com.example.rickandmorty.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val charactersEventChannel = Channel<CharactersEvent>()
    val charactersEvent = charactersEventChannel.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val charactersFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow.distinctUntilChanged(),
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
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

    fun onCharacterSelected(character: Character) = viewModelScope.launch {
        charactersEventChannel.send(CharactersEvent.NavigateToDetailScreen(character))
    }

    sealed class CharactersEvent {
        data class NavigateToDetailScreen(val character: Character) : CharactersEvent()
    }
}
