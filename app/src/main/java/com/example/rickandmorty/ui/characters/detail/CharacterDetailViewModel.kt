package com.example.rickandmorty.ui.characters.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.repository.Repository
import com.example.rickandmorty.model.Character
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val characterDetailEventChannel = Channel<CharacterDetailEvent>()
    val characterDetailEvent = characterDetailEventChannel.receiveAsFlow()

    fun saveCharacter(character: Character) = viewModelScope.launch {
        repository.insertCharacter(character)
        characterDetailEventChannel.send(
            CharacterDetailEvent.ShowSaveCharacterMessage(
                character
            )
        )
    }

    sealed class CharacterDetailEvent {
        data class ShowSaveCharacterMessage(val character: Character) :
            CharacterDetailEvent()
    }
}
