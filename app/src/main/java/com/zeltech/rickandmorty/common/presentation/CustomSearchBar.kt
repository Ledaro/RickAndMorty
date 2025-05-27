package com.zeltech.rickandmorty.common.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.zeltech.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun CustomSearchBar(
    query: String,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
) {
    TextField(
        value = query,
        onValueChange = { onQueryChange(it) },
        modifier = modifier,
        placeholder = {
            Text(
                text = "Search"
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Preview
@Composable
fun CustomSearchBarPreview() {
    RickAndMortyTheme {
        CustomSearchBar(
            query = "",
            onQueryChange = {}
        )
    }
}
