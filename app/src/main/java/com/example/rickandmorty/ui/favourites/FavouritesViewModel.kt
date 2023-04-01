package com.example.rickandmorty.ui.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.repository.Repository
import com.example.rickandmorty.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val favouritesFavouritesCharactersEventChannel = Channel<FavouritesCharactersEvent>()
    val favouritesCharactersEvent = favouritesFavouritesCharactersEventChannel.receiveAsFlow()

    val savedCharacters = repository.getSavedCharacters().asLiveData()

    fun swipeDeleteCharacter(character: Character) = viewModelScope.launch {
        repository.deleteCharacter(character)
        favouritesFavouritesCharactersEventChannel.send(
            FavouritesCharactersEvent.ShowUndoDeleteCharacterMessage(
                character
            )
        )
    }

    fun undoSwipeDeleteCharacter(character: Character) = viewModelScope.launch {
        repository.insertCharacter(character)
    }

    sealed class FavouritesCharactersEvent {
        data class ShowUndoDeleteCharacterMessage(val character: Character) :
            FavouritesCharactersEvent()
    }
}
