package com.zeltech.rickandmorty.features.characters.presentation.components.filters.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.status.model.Status
import com.zeltech.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun StatusesFilter(
    selectedStatus: Status? = null,
    statuses: List<Status> = Status.entries,
    onStatusSelected: (Status?) -> Unit = {},
) {
    Column {
        Text(
            text = "Status",
            color = MaterialTheme.colorScheme.primary,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            statuses.forEach { status ->
                val isCurrentlySelected = selectedStatus == status
                FilterChip(
                    selected = isCurrentlySelected,
                    onClick = {
                        if (isCurrentlySelected) {
                            onStatusSelected(null)
                        } else {
                            onStatusSelected(status)
                        }
                    },
                    label = { Text(status.displayName) },
                    trailingIcon =
                        if (isCurrentlySelected) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                )
                            }
                        } else {
                            null
                        },
                    // colors = FilterColors(),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun StatusesFilterPreview() {
    RickAndMortyTheme {
        StatusesFilter()
    }
}
