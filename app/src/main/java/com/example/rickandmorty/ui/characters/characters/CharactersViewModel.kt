package com.example.rickandmorty.ui.characters.characters

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.rickandmorty.data.repository.Repository
import com.example.rickandmorty.util.Constants.Companion.STATUS_ALIVE
import com.example.rickandmorty.util.Constants.Companion.STATUS_ALL
import com.example.rickandmorty.util.Constants.Companion.STATUS_DEAD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: Repository,
    private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("search_query", "")
    val characterStatus = MutableStateFlow(CharacterStatus.ALL)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val charactersFlow = combine(
        searchQuery.asFlow(),
        characterStatus
    ) { query, characterStatus ->
        Pair(query, characterStatus)
    }.flatMapLatest { (query, characterStatus) ->
        delay(500L)
        when (characterStatus) {
            CharacterStatus.ALIVE -> repository.getSearchResults(query, STATUS_ALIVE)
                .cachedIn(viewModelScope)
            CharacterStatus.DEAD -> repository.getSearchResults(query, STATUS_DEAD)
                .cachedIn(viewModelScope)
            CharacterStatus.ALL -> repository.getSearchResults(query, STATUS_ALL)
                .cachedIn(viewModelScope)
        }
    }

    val characters = charactersFlow.asLiveData()
}

enum class CharacterStatus { ALIVE, DEAD, ALL }
