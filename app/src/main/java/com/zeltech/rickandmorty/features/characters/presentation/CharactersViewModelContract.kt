package com.zeltech.rickandmorty.features.characters.presentation

import com.zeltech.rickandmorty.common.domain.model.Character

object CharactersViewModelContract {
    data class UiState(
        val characters: List<Character>? = null,
        val query: String = "",
        val isLoading: Boolean = false
    )
}
