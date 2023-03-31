package com.example.rickandmorty.ui.characters.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.data.repository.Repository
import com.example.rickandmorty.util.Constants.Companion.STATUS_ALIVE
import com.example.rickandmorty.util.Constants.Companion.STATUS_ALL
import com.example.rickandmorty.util.Constants.Companion.STATUS_DEAD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val characterStatus = MutableStateFlow(CharacterStatus.ALL)

    private val charactersFlow = combine(
        searchQuery,
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
