package com.example.rickandmorty.ui.characters.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.util.Constants.Companion.DEFAULT_SEARCH_QUERY
import com.example.rickandmorty.data.repository.Repository
import com.example.rickandmorty.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_SEARCH_QUERY)

    fun searchCharacters(query: String) {
        currentQuery.value = query
    }

    val characters = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun saveCharacter(character: Character) = viewModelScope.launch {
        repository.insertCharacter(character)
    }
}
