package com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.common.FilterColors
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.model.Gender
import com.zeltech.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun GendersFilter(
    selectedGender: Gender? = null,
    genders: List<Gender> = Gender.entries,
    onGenderSelected: (Gender) -> Unit = {},
) {
    Column {
        Text(
            text = "Gender",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            genders.forEach { gender ->
                val selected = selectedGender == gender
                FilterChip(
                    selected = selected,
                    onClick = {
                        onGenderSelected(gender)
                    },
                    label = { Text(gender.displayName) },
                    leadingIcon =
                        if (selected) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                )
                            }
                        } else {
                            null
                        },
                    //colors = FilterColors(),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun GenderFilterPreview() {
    RickAndMortyTheme {
        GendersFilter()
    }
}
