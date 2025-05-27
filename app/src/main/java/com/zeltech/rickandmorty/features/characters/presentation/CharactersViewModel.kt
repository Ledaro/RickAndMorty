package com.zeltech.rickandmorty.features.characters.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeltech.rickandmorty.features.characters.domain.CharactersService
import com.zeltech.rickandmorty.features.characters.presentation.CharactersViewModelContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersService: CharactersService
) : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        getAllCharacters()

        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    performSearch(query)
                }
        }
    }

    private fun getAllCharacters() {
        _state.update { currentState ->
            currentState.copy(isLoading = true)
        }

        viewModelScope.launch {
            try {
                val characters = charactersService.getAllCharacters()?.results
                _state.update { currentState ->
                    currentState.copy(
                        characters = characters,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("CharactersViewModel", "Error fetching characters", e)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _state.update { currentState ->
            currentState.copy(
                query = query
            )
        }
    }

    private fun performSearch(query: String) {
        _state.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }

        viewModelScope.launch {
            try {
                val characters = charactersService.getFilteredCharacters(query)?.results
                _state.update { currentState ->
                    currentState.copy(
                        characters = characters,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("CharactersViewModel", "Error searching characters", e)
            }
        }
    }
}
