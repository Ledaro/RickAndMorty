package com.example.rickandmorty.ui.characters.characters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.util.Constants.Companion.DEFAULT_SEARCH_QUERY
import com.example.rickandmorty.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_SEARCH_QUERY)

    fun searchCharacters(query: String) {
        currentQuery.value = query
    }

    val characters = currentQuery.switchMap { queryString ->
        apiRepository.getSearchResults(queryString).cachedIn(viewModelScope)
    }
}
