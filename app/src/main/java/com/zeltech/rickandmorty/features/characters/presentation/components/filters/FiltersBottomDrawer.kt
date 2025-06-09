package com.zeltech.rickandmorty.features.characters.presentation.components.filters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.GendersFilter
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.gender.model.Gender
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.status.StatusesFilter
import com.zeltech.rickandmorty.features.characters.presentation.components.filters.status.model.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomDrawer(
    sheetState: SheetState = rememberModalBottomSheetState(),
    selectedStatus: Status? = null,
    selectedGender: Gender? = null,
    onStatusSelected: (Status?) -> Unit,
    onGenderSelected: (Gender?) -> Unit,
    onClearFiltersClick: () -> Unit,
    onApplyFiltersButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = null,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(8.dp)
                    .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Filter by: ",
                    style = MaterialTheme.typography.bodyLarge,
                )

                if (selectedStatus != null || selectedGender != null) {
                    Text(
                        text = "Clear All",
                        modifier = Modifier.clickable { onClearFiltersClick() },
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            StatusesFilter(
                selectedStatus = selectedStatus,
                onStatusSelected = onStatusSelected,
            )
            GendersFilter(
                selectedGender = selectedGender,
                onGenderSelected = onGenderSelected,
            )
            Button(
                onClick = onApplyFiltersButtonClick,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
            ) {
                Text("Apply")
            }
        }
    }
}
