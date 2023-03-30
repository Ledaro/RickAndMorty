package com.example.rickandmorty.ui.characters.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.data.repository.Repository
import com.example.rickandmorty.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val charactersFlow = searchQuery.flatMapLatest {
        repository.getSearchResults(it, "alive").cachedIn(viewModelScope)
    }

    val characters = charactersFlow.asLiveData()

    fun saveCharacter(character: Character) = viewModelScope.launch {
        repository.insertCharacter(character)
    }
}
