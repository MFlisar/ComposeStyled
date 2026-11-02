package com.michaelflisar.demo.demos.regions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.specials.StyledGroupBox
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoGroupBox(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Group Box",
        id = baseId + 1,
        expanded = expandState,
    ) {
        val modifier = Modifier.fillMaxWidth()

        val groupBoxClicks = rememberSaveable { mutableIntStateOf(0) }

        SubTitle("Clickable, default border: GroupBox draws border")
        StyledGroupBox(
            modifier = modifier,
            label = "Name",
            onClick = { groupBoxClicks.intValue++ },
        ) {
            StyledText("Content inside the group box (${groupBoxClicks.intValue} clicks)")
        }
        SubTitle("Clickable, custom border: 2 dp GroupBox border")
        StyledGroupBox(
            modifier = modifier,
            label = "Custom border",
            onClick = { },
            border = BorderStroke(2.dp, StyledTheme.colorScheme.primary),
        ) {
            StyledText("Clickable with a custom 2 dp border")
        }
        StyledGroupBox(
            modifier = modifier,
            label = "Email",
            containerColor = StyledTheme.colorScheme.primarySurface,
        ) {
            StyledText("Non-clickable group box (no StyledButton)")
        }
    }
}
