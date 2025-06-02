package com.zeltech.rickandmorty.features.characters.presentation

import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.Gender

object CharactersViewModelContract {
    data class UiState(
        val characters: List<Character>? = null,
        val query: String = "",
        val isLoading: Boolean = false,
        val selectedGender: Gender? = null
    )
}
