package com.zeltech.rickandmorty.features.characters.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zeltech.rickandmorty.R
import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.common.presentation.CustomSearchBar
import com.zeltech.rickandmorty.features.characters.presentation.components.CharacterItem
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.Gender
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.GenderFilter
import com.zeltech.rickandmorty.ui.theme.RickAndMortyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen() {
    val viewModel = hiltViewModel<CharactersViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    StatelessCharactersScreen(
        characters = state.characters,
        query = state.query,
        isLoading = state.isLoading,
        selectedGender = state.selectedGender,
        onQueryChange = viewModel::onSearchQueryChanged,
        onGenderSelected = viewModel::onGenderSelected,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessCharactersScreen(
    characters: List<Character>?,
    query: String = "",
    isLoading: Boolean = false,
    selectedGender: Gender? = null,
    onQueryChange: (String) -> Unit = {},
    onGenderSelected: (Gender) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.padding(horizontal = 8.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Characters")
                },
                actions = {
                    IconButton(
                        onClick = {
                            showBottomSheet = true
                        },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter_circle),
                            contentDescription = "Search",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                dragHandle = null,
            ) {
                GenderFilter(
                    selectedGender = selectedGender,
                    onGenderSelected = onGenderSelected,
                )
            }
        }

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            characters?.let { characters ->
                stickyHeader {
                    CustomSearchBar(
                        query = query,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                        onQueryChange = onQueryChange,
                    )
                }
                if (isLoading) {
                    item {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    items(characters) { character ->
                        CharacterItem(character)
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun CharactersScreenPreview() {
    RickAndMortyTheme {
        StatelessCharactersScreen(
            characters = listOf(),
        )
    }
}
