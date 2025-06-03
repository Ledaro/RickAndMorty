package com.zeltech.rickandmorty.features.characters.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeltech.rickandmorty.features.characters.domain.CharactersService
import com.zeltech.rickandmorty.features.characters.presentation.CharactersViewModelContract.UiState
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.model.Gender
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.status.model.Status
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
        private val _selectedStatusFlow = MutableStateFlow<Status?>(null)

        init {
            getAllCharacters()

            viewModelScope.launch {
                combine(
                    _searchQueryFlow.debounce(500).distinctUntilChanged(),
                    _selectedStatusFlow,
                    _selectedGenderFlow,
                ) { query, status, gender ->
                    Triple(query, status, gender)
                }.collectLatest { (query, status, gender) ->
                    performSearchWithFilters(query, status, gender)
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
            status: Status?,
            gender: Gender?,
        ) {
            _state.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }

            viewModelScope.launch {
                try {
                    val characters =
                        charactersService
                            .getFilteredCharacters(
                                query,
                                status?.displayName,
                                gender?.displayName,
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

        fun onStatusSelected(status: Status?) {
            _state.update { currentState ->
                currentState.copy(selectedStatus = status)
            }
            status?.let {
                _selectedStatusFlow.value = status
            }
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
