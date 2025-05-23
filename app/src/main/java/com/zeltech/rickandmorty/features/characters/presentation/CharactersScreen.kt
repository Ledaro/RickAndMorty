package com.zeltech.rickandmorty.features.characters.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zeltech.rickandmorty.common.domain.model.Character

@Composable
fun CharactersScreen() {
    val viewModel = hiltViewModel<CharactersViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            state.characters?.let { characters ->
                items(characters) { character ->
                    CharacterItem(character)
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: Character) {
    Text(text = character.id ?: "")
}
