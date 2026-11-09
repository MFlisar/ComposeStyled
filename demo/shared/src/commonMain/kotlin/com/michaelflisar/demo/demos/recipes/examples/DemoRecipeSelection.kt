package com.michaelflisar.demo.demos.recipes.examples

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.platform.SystemBarStyleEffectIfNeeded
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.components.StyledSelectionToolbar
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.LocalKeyboardModifiers
import com.michaelflisar.composestyled.ui.state.SelectionState
import com.michaelflisar.composestyled.ui.state.handleListItemClick
import com.michaelflisar.composestyled.ui.state.rememberSaveableSelectionState
import com.michaelflisar.demo.helper.DemoTopBar

@Composable
internal fun DemoRecipeSelection(
    onBack: () -> Unit,
) {
    val items = remember {
        listOf(
            "Design notes",
            "Image assets",
            "Release checklist",
            "Source archive",
            "Meeting summary",
        )
    }
    val selection = rememberSaveableSelectionState(keySaver = SelectionState.StringSaver)
    val keyboardModifierState = LocalKeyboardModifiers.current
    val modifiers = keyboardModifierState.modifiers
    val scaffoldColor = StyledTheme.colorScheme.surface
    val topBarColor = StyledTheme.colorScheme.primary

    SystemBarStyleEffectIfNeeded(
        statusBarColor = topBarColor,
        navigationBarColor = scaffoldColor,
        backgroundColor = scaffoldColor,
    )

    StyledScaffold(
        containerColor = scaffoldColor,
        topBar = {
            DemoTopBar(
                containerColor = topBarColor,
                title = "Selection",
                onBack = onBack,
            )
        },
    ) { paddingValues ->
        StyledColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(StyledTheme.padding.content),
            itemSpacing = StyledTheme.spacing.medium,
        ) {
            StyledText(
                "Android/iOS: tap to toggle an item. Desktop/Web: click selects one item; " +
                    "Ctrl/Meta-click toggles; Shift-click selects a range."
            )
            StyledText(
                "Modifiers: Ctrl ${modifiers.ctrl}, Meta ${modifiers.meta}, " +
                    "Shift ${modifiers.shift}, Alt ${modifiers.alt}"
            )
            StyledSelectionToolbar(
                selection = selection,
                containerColor = StyledTheme.colorScheme.primarySurface,
                contentColor = StyledTheme.colorScheme.onPrimarySurface,
                title = { count -> StyledText("$count / ${items.size} selected") },
                actions = {
                    StyledButton(
                        onClick = { selection.clearSelection() }
                    ) {
                        StyledText("Clear")
                    }

                    StyledButton(
                        onClick = { selection.selectAll(items) }
                    ) {
                        StyledText("Select all")
                    }
                },
            )
            items.forEachIndexed { index, item ->
                StyledListItem(
                    headline = {
                        StyledText(item)
                    },
                    supporting = {
                        StyledText("Index: $index | Selected: ${selection.isSelected(item)}")
                    },
                    leading = {
                        if (selection.isSelected(item)) {
                            StyledIcon(Icons.Default.Check.toIconComposable())
                        } else {
                            StyledIcon(Icons.Default.CheckBoxOutlineBlank.toIconComposable())
                        }
                    },
                    onClick = {
                        selection.handleListItemClick(
                            index = index,
                            items = items,
                            keyboardModifierState = keyboardModifierState,
                            key = { it },
                        )
                    },
                    shape = StyledTheme.shapes.medium,
                )
            }
        }
    }
}
