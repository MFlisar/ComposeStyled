package com.michaelflisar.demo.demos

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyledcomponents.ui.components.StyledSegmentedButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledSegmentedButtonRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledSegmentedButtonRowDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledSegmentedButtonRowMultiSelect
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoSegmentedButtons(
    baseId: Int,
    expandState: ExpandState,
) {
    val items = listOf("List", "Grid", "Gallery", "Details", "Settings", "Profile", "Logout")
    val items2 = listOf(
        "List view with a longer label",
        "Grid view with additional details",
        "Gallery view with large previews",
    )
    val segmentedSelection = remember { mutableStateOf(items.first()) }
    val segmentedSelection2 = remember { mutableStateOf(items2.first()) }
    val segmentedMultiSelection = remember { mutableStateOf(listOf(items.first())) }
    val singleSegmentSelected = remember { mutableStateOf(false) }

    val content = StyledSegmentedButtonRowDefaults.defaultRenderer<String>()

    RegionContainer(
        title = "Segmented Buttons",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Single choice")
        StyledSegmentedButtonRow(
            items = items,
            selected = segmentedSelection,
            content = content,
        )
        SubTitle("Long labels / stable check slot")
        StyledSegmentedButtonRow(
            items = items2,
            selected = segmentedSelection2,
            content = content,
        )
        StyledSegmentedButtonRow(
            items = items2,
            selected = segmentedSelection2,
            modifier = Modifier.fillMaxWidth(),
            content = content,
        )

        SubTitle("Multi choice")
        StyledSegmentedButtonRowMultiSelect(
            items = items,
            selected = segmentedMultiSelection,
            content = content,
        )

        SubTitle("Single segment")
        StyledSegmentedButton(
            selected = singleSegmentSelected.value,
            onClick = { singleSegmentSelected.value = !singleSegmentSelected.value },
        ) {
            StyledText("Standalone segment")
        }
    }
}
