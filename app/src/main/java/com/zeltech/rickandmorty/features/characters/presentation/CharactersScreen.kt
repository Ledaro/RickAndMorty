package com.zeltech.rickandmorty.features.characters.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.zeltech.rickandmorty.R
import com.zeltech.rickandmorty.common.domain.model.Character
import com.zeltech.rickandmorty.common.presentation.CustomSearchBar
import com.zeltech.rickandmorty.features.characters.presentation.components.CharacterItem
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.FiltersBottomDrawer
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.model.Gender
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.status.model.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen() {
    val viewModel = hiltViewModel<CharactersViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val characters = viewModel.charactersPagingDataFlow.collectAsLazyPagingItems()

    StatelessCharactersScreen(
        characters = characters,
        query = state.query,
        selectedGender = state.pendingSelectedGender,
        selectedStatus = state.pendingSelectedStatus,
        filterCount = state.activeFilterCount,
        onQueryChange = viewModel::onSearchQueryChanged,
        onStatusSelected = viewModel::onPendingStatusSelected,
        onGenderSelected = viewModel::onPendingGenderSelected,
        onClearFiltersClick = viewModel::onClearFiltersClicked,
        onApplyFiltersButtonClick = viewModel::onApplyFiltersClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessCharactersScreen(
    characters: LazyPagingItems<Character>,
    query: String = "",
    selectedStatus: Status? = null,
    selectedGender: Gender? = null,
    filterCount: Int,
    onQueryChange: (String) -> Unit,
    onStatusSelected: (Status?) -> Unit,
    onGenderSelected: (Gender?) -> Unit,
    onClearFiltersClick: () -> Unit,
    onApplyFiltersButtonClick: () -> Unit,
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Characters") },
                actions = {
                    BadgedBox(
                        badge = {
                            if (filterCount > 0) {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White,
                                ) { Text("$filterCount") }
                            }
                        },
                    ) {
                        IconButton(onClick = { showBottomSheet = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_filter_circle),
                                contentDescription = "Open Filters",
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        if (showBottomSheet) {
            FiltersBottomDrawer(
                selectedStatus = selectedStatus,
                selectedGender = selectedGender,
                onStatusSelected = onStatusSelected,
                onGenderSelected = onGenderSelected,
                onClearFiltersClick = onClearFiltersClick,
                onApplyFiltersButtonClick = {
                    onApplyFiltersButtonClick()
                    showBottomSheet = false
                },
                onDismissRequest = { showBottomSheet = false },
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CustomSearchBar(
                query = query,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp, bottom = 8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                onQueryChange = onQueryChange,
            )

            Box(
                modifier = Modifier
                    .weight(1f) // If inside a Column that also has the search bar
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // THIS IS THE KEY PART for the main loading indicator
                when (val refreshState = characters.loadState.refresh) {
                    is LoadState.Loading -> {
                        // This is when the Pager is doing a full refresh (initial load or after parameter change)
                        CircularProgressIndicator()
                    }

                    is LoadState.Error -> {
                        val error = refreshState.error
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Error: ${error.localizedMessage ?: "Unknown error"}",
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { characters.retry() }) {
                                Text("Retry Refresh")
                            }
                        }
                    }

                    is LoadState.NotLoading -> {
                        // Refresh is NotLoading. Now check if the list is empty.
                        // Also check append/prepend states to ensure we are not in the middle of loading more.
                        if (characters.itemCount == 0 &&
                            !(characters.loadState.append is LoadState.Loading ||
                                    characters.loadState.prepend is LoadState.Loading ||
                                    characters.loadState.source.refresh is LoadState.Loading // Additional check
                                    )
                        ) {
                            Text("No characters found.")
                        } else {
                            // Display the list
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                            ) {
                                items(characters.itemCount) { index ->
                                    val character = characters[index]
                                    if (character != null) {
                                        CharacterItem(character)
                                    }
                                }

                                // Handle append loading state (for pagination spinner at the bottom)
                                when (val appendState =
                                    characters.loadState.append) {
                                    is LoadState.Loading -> {
                                        item {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 16.dp),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                CircularProgressIndicator()
                                            }
                                        }
                                    }

                                    is LoadState.Error -> {
                                        item {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    "Error loading more.",
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                                Button(onClick = { characters.retry() }) {
                                                    Text("Retry Load More")
                                                }
                                            }
                                        }
                                    }

                                    else -> { /* NotLoading or EndOfPaginationReached */
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

//@PreviewLightDark
//@Composable
//fun CharactersScreenPreview() {
//    RickAndMortyTheme {
//        StatelessCharactersScreen(
//            characters = listOf(),
//            query = "",
//            isLoading = false,
//            selectedGender = null,
//            selectedStatus = null,
//            filterCount = 0,
//            onQueryChange = {},
//            onStatusSelected = {},
//            onGenderSelected = {},
//            onClearFiltersClick = {},
//            onApplyFiltersButtonClick = {},
//        )
//    }
//}
