package com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.runtime.Composable

@Composable
fun FilterColors(): SelectableChipColors =
    SelectableChipColors(
        containerColor = MaterialTheme.colorScheme.surface,
        labelColor = MaterialTheme.colorScheme.secondary,
        leadingIconColor = MaterialTheme.colorScheme.secondary,
        trailingIconColor = MaterialTheme.colorScheme.secondary,
        disabledContainerColor = MaterialTheme.colorScheme.tertiary,
        disabledLabelColor = MaterialTheme.colorScheme.tertiary,
        disabledLeadingIconColor = MaterialTheme.colorScheme.tertiary,
        disabledTrailingIconColor = MaterialTheme.colorScheme.tertiary,
        selectedContainerColor = MaterialTheme.colorScheme.surface,
        disabledSelectedContainerColor = MaterialTheme.colorScheme.tertiary,
        selectedLabelColor = MaterialTheme.colorScheme.primary,
        selectedLeadingIconColor = MaterialTheme.colorScheme.primary,
        selectedTrailingIconColor = MaterialTheme.colorScheme.primary,
    )
