package com.michaelflisar.demo.helper

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledDropdownIndex
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedColumn
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.ui.components.StyledSecondaryButton

@Composable
internal fun DropdownWithButtons(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedIndex: MutableState<Int>,
) {
    StyledSpacedColumn(
        modifier = modifier,
        itemSpacing = StyledTheme.spacing.small
    ) {
        StyledDropdownIndex(
            modifier = Modifier.fillMaxWidth(),
            items = items,
            selectedIndex = selectedIndex
        )

        StyledSpacedRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            StyledSecondaryButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (selectedIndex.value == 0) {
                        selectedIndex.value = items.size - 1
                    } else {
                        selectedIndex.value--
                    }
                }
            ) {
                StyledIcon(
                    icon = Icons.AutoMirrored.Filled.ArrowBack.toIconComposable()
                )
            }

            StyledSecondaryButton(

                modifier = Modifier.weight(1f),
                onClick = {
                    if (selectedIndex.value == items.size - 1) {
                        selectedIndex.value = 0
                    } else {
                        selectedIndex.value++
                    }
                }
            ) {
                StyledIcon(
                    icon = Icons.AutoMirrored.Filled.ArrowForward.toIconComposable()
                )
            }
        }
    }

}