package com.example.rickandmorty.ui.characters

import androidx.lifecycle.ViewModel
import com.example.rickandmorty.data.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val apiRepository: ApiRepository) :
    ViewModel() {
}
