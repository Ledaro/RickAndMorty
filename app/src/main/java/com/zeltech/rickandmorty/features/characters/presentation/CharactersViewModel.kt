package com.zeltech.rickandmorty.features.characters.presentation

import android.util.Log
import androidx.activity.result.launch
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zeltech.CharactersQuery
import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.features.characters.data.CharactersPagingSource
import com.zeltech.rickandmorty.features.characters.domain.CharactersService
import com.zeltech.rickandmorty.features.characters.presentation.CharactersViewModelContract.UiState
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.model.Gender
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.status.model.Status
import com.zeltech.type.Characters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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

    private val _appliedQuery = MutableStateFlow("")
    private val _appliedStatus = MutableStateFlow<Status?>(null)
    private val _appliedGender = MutableStateFlow<Gender?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val charactersPagingDataFlow: Flow<PagingData<Character>> =
        combine(
            _appliedQuery,
            _appliedStatus,
            _appliedGender
        ) { query, status, gender ->
            // Triple to hold the parameters that will trigger a new PagingSource
            Triple(query, status, gender)
        }
            .distinctUntilChanged() // Only proceed if the combined params actually changed
            .flatMapLatest { (query, status, gender) -> // When params change, create new Pager flow
                Pager(
                    config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                    pagingSourceFactory = {
                        // isLoading will be set to false by observing LoadStates from PagingDataAdapter
                        CharactersPagingSource(
                            service = charactersService,
                            name = if (query.isNotBlank()) query else null,
                            status = status?.displayName,
                            gender = gender?.displayName
                        )
                    }
                ).flow
            }
            .cachedIn(viewModelScope)

    init {
        // You might still want a debounced flow for the input text field
        // if you want to update _uiState.query immediately but _appliedQuery with debounce.
        viewModelScope.launch {
            _state.map { it.query } // Observe the query from UI state
                .debounce(500L) // Debounce for UI responsiveness if needed, Pager uses its own debounce on _appliedQuery
                .distinctUntilChanged()
                .collectLatest { queryFromUi ->
                    // This is where we update the StateFlow that drives the Pager
                    _appliedQuery.value = queryFromUi
                }
        }
    }

    private fun calculateAppliedFiltersCount(
        status: Status?,
        gender: Gender?,
    ): Int {
        var count = 0
        if (gender != null) count++
        if (status != null) count++
        return count
    }

    fun onSearchQueryChanged(newQuery: String) {
        _state.update { currentState ->
            currentState.copy(query = newQuery)
        }
    }

    fun onPendingStatusSelected(status: Status?) {
        _state.update { currentState ->
            currentState.copy(pendingSelectedStatus = status)
        }
    }

    fun onPendingGenderSelected(gender: Gender?) {
        _state.update { currentState ->
            currentState.copy(pendingSelectedGender = gender)
        }
    }

    fun onApplyFiltersClicked() {
        val pendingStatus = _state.value.pendingSelectedStatus
        val pendingGender = _state.value.pendingSelectedGender

        _state.update { currentState ->
            currentState.copy(
                appliedSelectedStatus = pendingStatus,
                appliedSelectedGender = pendingGender,
                activeFilterCount = calculateAppliedFiltersCount(pendingStatus, pendingGender),
            )
        }

        _appliedStatus.value = pendingStatus
        _appliedGender.value = pendingGender
    }

    fun onClearFiltersClicked() {
        _state.update { currentState ->
            currentState.copy(
                pendingSelectedStatus = null,
                pendingSelectedGender = null,
                activeFilterCount = 0,
            )
        }
    }
}
