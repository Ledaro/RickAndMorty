package com.example.rickandmorty.ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.rickandmorty.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    val savedCharacters = repository.getSavedCharacters()
}
