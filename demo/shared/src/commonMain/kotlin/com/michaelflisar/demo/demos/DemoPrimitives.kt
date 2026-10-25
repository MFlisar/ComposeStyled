package com.michaelflisar.demo.demos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedRow
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledExpandIcon
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledHorizontalSpacer
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledInputAction
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledVerticalSpacer
import com.michaelflisar.demo.helper.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoPrimitives(
    baseId: Int,
    expandState: ExpandState,
    showMessage: (String) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }

    RegionContainer(
        title = "Text, spacing and input actions",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Styled text")
        StyledText("Text inherits the current theme style and content color.")
        StyledText(
            text = "Text can also set explicit emphasis and color.",
            color = StyledTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )

        SubTitle("Spacers")
        StyledText("Content before a horizontal spacer")
        StyledHorizontalSpacer(size = 12.dp)
        StyledText("Content after a horizontal spacer")
        StyledSpacedRow(verticalAlignment = Alignment.CenterVertically) {
            StyledText("Before vertical spacer")
            StyledVerticalSpacer(size = 24.dp)
            StyledText("After vertical spacer")
        }

        SubTitle("Input actions and expand icon")
        StyledInputAction(
            icon = Icons.Default.Add.toIconComposable(),
            onClick = { showMessage("Input action clicked") },
            contentDescription = "Add",
        )
        StyledSpacedRow(verticalAlignment = Alignment.CenterVertically) {
            StyledText("Toggle details")
            StyledInputAction(
                onClick = { expanded.value = !expanded.value },
            ) {
                StyledExpandIcon(expanded = expanded.value)
            }
        }
        if (expanded.value) {
            StyledText("The expand icon reflects the current state.")
        }
    }
}
