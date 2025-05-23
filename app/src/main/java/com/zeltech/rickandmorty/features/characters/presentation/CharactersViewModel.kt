package com.zeltech.rickandmorty.features.characters.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.features.characters.domain.CharactersService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersService: CharactersService
) : ViewModel() {

    data class CharactersState(
        val characters: List<Character>? = null
    )

    private val _state = MutableStateFlow(CharactersState())
    val state: StateFlow<CharactersState> = _state.asStateFlow()

    init {
        getAllCharacters()
    }

    private fun getAllCharacters() {
        viewModelScope.launch {
            try {
                val characters = charactersService.getAllCharacters()?.results
                _state.update { currentState ->
                    currentState.copy(characters = characters)
                }
            } catch (e: Exception) {
                Log.e("CharactersViewModel", "Error fetching characters", e)
            }
        }
    }
}
