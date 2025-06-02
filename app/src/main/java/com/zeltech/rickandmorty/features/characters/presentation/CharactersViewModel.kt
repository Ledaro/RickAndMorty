package com.zeltech.rickandmorty.features.characters.presentation

import android.util.Log
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeltech.rickandmorty.features.characters.domain.CharactersService
import com.zeltech.rickandmorty.features.characters.presentation.CharactersViewModelContract.UiState
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.Gender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CharactersViewModel
    @Inject
    constructor(
        private val charactersService: CharactersService,
    ) : ViewModel() {
        private val _state = MutableStateFlow(UiState())
        val state: StateFlow<UiState> = _state.asStateFlow()

        private val _searchQueryFlow = MutableStateFlow("")
        private val _selectedGenderFlow = MutableStateFlow<Gender?>(null)

        init {
            getAllCharacters()

            viewModelScope.launch {
                combine(
                    _searchQueryFlow.debounce(500).distinctUntilChanged(),
                    _selectedGenderFlow,
                ) { query, gender ->
                    Pair(query, gender)
                }.collectLatest { (query, gender) ->
                    performSearchWithFilters(query, gender)
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
                            isLoading = false,
                        )
                    }
                } catch (e: Exception) {
                    Log.e("CharactersViewModel", "Error fetching characters", e)
                }
            }
        }

        private fun performSearchWithFilters(
            query: String,
            gender: Gender?,
        ) {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }

            val genderFilterValue = gender?.displayName
            viewModelScope.launch {
                try {
                    val characters =
                        charactersService
                            .getFilteredCharacters(
                                query,
                                genderFilterValue,
                            )?.results
                    _state.update { currentState ->
                        currentState.copy(
                            characters = characters,
                            isLoading = false,
                        )
                    }
                } catch (e: Exception) {
                    Log.e("CharactersViewModel", "Error searching characters", e)
                }
            }
        }

        fun onSearchQueryChanged(newQuery: String) {
            _state.update { currentState ->
                currentState.copy(query = newQuery)
            }
            _searchQueryFlow.value = newQuery
        }

        fun onGenderSelected(gender: Gender?) {
            _state.update { currentState ->
                currentState.copy(selectedGender = gender)
            }
            gender?.let {
                _selectedGenderFlow.value = gender
            }
        }
    }
