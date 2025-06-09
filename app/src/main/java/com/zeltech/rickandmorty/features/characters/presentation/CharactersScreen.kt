package com.zeltech.rickandmorty.features.characters.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
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
import com.zeltech.rickandmorty.ui.theme.RickAndMortyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen() {
    val viewModel = hiltViewModel<CharactersViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagingItems = viewModel.pager.collectAsLazyPagingItems()

    StatelessCharactersScreen(
        pagingItems = pagingItems,
        characters = state.characters,
        query = state.query,
        isLoading = state.isLoading,
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
    pagingItems: LazyPagingItems<Character>,
    isLoading: Boolean = false,
    characters: List<Character>?,
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
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (characters.isNullOrEmpty()) {
                    Text("No characters found.")
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                    ) {
                        items(pagingItems.itemCount) { index ->
                            val character = pagingItems[index]
                            if (character != null) {
                                CharacterItem(character)
                            }
                        }

                        pagingItems.apply {
                            when {
                                loadState.append is LoadState.Loading -> {
                                    item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
                                }

                                loadState.refresh is LoadState.Error -> {
                                    item { Text("Błąd ładowania danych") }
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
