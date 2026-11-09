package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButton
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButtonRow
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButtonRowDefaults
import com.michaelflisar.composestyled.ui.components.StyledSegmentedButtonRowMultiSelect
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
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
    val segmentedSelection = rememberSaveable { mutableStateOf(items.first()) }
    val segmentedSelection2 = rememberSaveable { mutableStateOf(items2.first()) }
    val segmentedMultiSelection = rememberSaveable { mutableStateOf(listOf(items.first())) }
    val segmentedMultiSelection2 = rememberSaveable { mutableStateOf(listOf(items2.first())) }
    val singleSegmentSelected = rememberSaveable { mutableStateOf(false) }

    val content = StyledSegmentedButtonRowDefaults.defaultRenderer<String>()

    RegionContainer(
        title = "Segmented Buttons",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Single choice (without selected icon)")
        StyledSegmentedButtonRow(
            items = items,
            selected = segmentedSelection,
            showSelectedIcon = false,
            content = content,
        )
        SubTitle("Wrapped labels / shared row height")
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
        SubTitle("Multi choice / wrapped labels")
        StyledSegmentedButtonRowMultiSelect(
            items = items2,
            selected = segmentedMultiSelection2,
            modifier = Modifier.fillMaxWidth(),
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
