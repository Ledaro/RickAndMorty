package com.zeltech.rickandmorty.features.characters.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.common.presentation.CustomSearchBar
import com.zeltech.rickandmorty.ui.theme.RickAndMortyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen() {
    val viewModel = hiltViewModel<CharactersViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    StatelessCharactersScreen(
        characters = state.characters,
        query = state.query,
        onQueryChange = viewModel::searchCharacters
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessCharactersScreen(
    characters: List<Character>?,
    query: String = "",
    onQueryChange: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Characters") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CustomSearchBar(
                query = query,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape),
                onQueryChange = onQueryChange,
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                characters?.let { characters ->
                    items(characters) { character ->
                        CharacterItem(character)
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: Character) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Text(text = character.name ?: "")
    }
}

@PreviewLightDark
@Composable
fun CharactersScreenPreview() {
    RickAndMortyTheme {
        StatelessCharactersScreen(
            characters = listOf()
        )
    }
}
