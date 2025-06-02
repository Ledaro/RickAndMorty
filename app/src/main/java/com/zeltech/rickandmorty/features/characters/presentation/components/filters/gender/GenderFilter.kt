package com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zeltech.rickandmorty.R
import com.zeltech.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun GenderFilter(
    selectedGender: Gender? = null,
    onGenderSelected: (Gender) -> Unit = {},
) {
    val genders = Gender.entries
    //var selectedGender by remember { mutableStateOf<Gender?>(null) }

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
                    leadingIcon = {
                        if (selected) {
                            val painter =
                                when (gender) {
                                    Gender.MALE -> painterResource(R.drawable.ic_gender_male)
                                    Gender.FEMALE -> painterResource(R.drawable.ic_gender_female)
                                    Gender.AGENDER -> painterResource(R.drawable.ic_gender_agender)
                                    Gender.UNKNOWN -> painterResource(R.drawable.ic_gender_unknown)
                                }
                            Icon(
                                painter = painter,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize),
                            )
                        }
                    },
                    colors = FilterColors(),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun GenderFilterPreview() {
    RickAndMortyTheme {
        GenderFilter()
    }
}
