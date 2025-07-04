package com.zeltech.rickandmorty.features.characters.presentation

import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.model.Gender
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.status.model.Status

object CharactersViewModelContract {
    data class UiState(
        val query: String = "",
        val pendingSelectedGender: Gender? = null,
        val pendingSelectedStatus: Status? = null,
        val appliedSelectedGender: Gender? = null,
        val appliedSelectedStatus: Status? = null,
        val activeFilterCount: Int = 0,
    )
}
