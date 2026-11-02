package com.michaelflisar.demo.demos.regions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.UpdateStyledCustomization
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.ColorToken
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.cupertino.liquid.components.CupertinoLiquidListItem
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledListItem
import com.michaelflisar.composestyled.ui.components.StyledListItemDefaults
import com.michaelflisar.composestyled.ui.components.StyledSelectionToolbar
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.LocalKeyboardModifiers
import com.michaelflisar.composestyled.ui.state.handleListItemClick
import com.michaelflisar.composestyled.ui.state.rememberSaveableSelectionState
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoListItems(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "List Items",
        id = baseId + 1,
        expanded = expandState,
    ) {
        val mainClicks = rememberSaveable { mutableIntStateOf(0) }
        val actionClicks = rememberSaveable { mutableIntStateOf(0) }
        val selectionItems = remember {
            listOf("Design notes", "Image assets", "Release checklist", "Source archive")
        }
        val selection = rememberSaveableSelectionState(keySaver = StringSaver)
        val keyboardModifiers = LocalKeyboardModifiers.current
        val leading: @Composable () -> Unit = {
            StyledIcon(icon = Icons.Default.Info.toIconComposable(), contentDescription = null)
        }

        val actions: @Composable RowScope.() -> Unit = {
            StyledIconButton(
                onClick = { actionClicks.intValue++ },
                role = StyledIconButton.Role.Tertiary,
                icon = Icons.Default.MoreVert.toIconComposable(),
                contentDescription = "Independent action",
            )
        }

        SubTitle("Platform-aware multi-selection")
        StyledText(
            "Android/iOS: tap toggles an item. Desktop/Web: click selects one; " +
                    "Ctrl/Meta-click toggles; Shift-click selects a range."
        )
        StyledSelectionToolbar(
            selection = selection,
            actions = {
                StyledButton(
                    onClick = { selection.selectAll(selectionItems) },
                    role = StyledButton.Role.Tertiary,
                ) {
                    StyledText("Select all")
                }
            },
        )
        selectionItems.forEachIndexed { index, item ->
            StyledListItem(
                headline = { StyledText("${if (selection.isSelected(item)) "[x]" else "[ ]"} $item") },
                supporting = {
                    StyledText(if (selection.isSelected(item)) "Selected" else "Not selected")
                },
                onClick = {
                    selection.handleListItemClick(
                        index = index,
                        items = selectionItems,
                        keyboardModifierState = keyboardModifiers,
                        key = { it },
                    )
                },
                shape = StyledTheme.shapes.medium,
            )
        }

        SubTitle("Semantic one-, two- and three-line patterns")
        StyledListItem(headline = { StyledText("Headline only") })
        StyledListItem(
            headline = { StyledText("Headline with supporting text") },
            supporting = { StyledText("Supporting text") },
            leading = leading,
        )
        StyledListItem(
            headline = { StyledText("Headline with overline") },
            overline = { StyledText("OVERLINE") },
            leading = leading,
        )
        StyledListItem(
            headline = { StyledText("Three-line item") },
            supporting = { StyledText("Supporting text") },
            overline = { StyledText("OVERLINE") },
            leading = leading,
            actions = actions,
        )
        StyledListItem(
            headline = { StyledText("Wrapping") },
            supporting = { StyledText("Supporting text that wraps onto multiple lines in a narrow item.") },
            modifier = Modifier.width(240.dp),
            leading = leading,
        )

        SubTitle("Whole-item click with independent actions")
        StyledText("Item: ${mainClicks.intValue}; action: ${actionClicks.intValue}")
        StyledText("Click the item or empty space around the action; the action has its own callback.")
        StyledListItem(
            headline = { StyledText("Rounded item with an independent action") },
            onClick = { mainClicks.intValue++ },
            shape = StyledTheme.shapes.medium,
            leading = leading,
            actions = actions,
        )
        StyledListItem(
            headline = { StyledText("Disabled item click; action still enabled") },
            onClick = { mainClicks.intValue++ },
            enabled = false,
            shape = StyledTheme.shapes.medium,
            leading = leading,
            actions = actions,
        )
        StyledListItem(
            headline = { StyledText("Passive main area; action still enabled") },
            actions = actions,
        )

        SubTitle("Free content")
        StyledListItem(
            onClick = { mainClicks.intValue++ },
            shape = StyledTheme.shapes.medium,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            leading = leading,
            actions = actions,
        ) {
            StyledText("Custom column", style = StyledTheme.typography.titleSmall)
            StyledText("No automatic semantic line classification")
            StyledText(
                "A third child with its own style",
                style = StyledTheme.typography.labelSmall
            )
        }

        SubTitle("Customization and layout overrides")
        StyledListItem(
            headline = { StyledText("Custom colors and border") },
            supporting = { StyledText("Theme tokens are resolved during composition") },
            onClick = { mainClicks.intValue++ },
            shape = StyledTheme.shapes.medium,
            customization = StyledListItemDefaults.customize(
                containerColor = ColorToken.Theme.PrimarySurface,
                contentColor = ColorToken.Theme.OnPrimarySurface,
                border = ColorToken.Theme.Primary,
            ),
            leading = leading,
            actions = actions,
        )
        StyledListItem(
            headline = { StyledText("Asymmetric padding") },
            onClick = { mainClicks.intValue++ },
            shape = StyledTheme.shapes.medium,
            contentPadding = PaddingValues(start = 24.dp, top = 16.dp, end = 8.dp, bottom = 12.dp),
            spacing = 20.dp,
            leading = leading,
            actions = actions,
        )

        SubTitle("Scoped renderer replacement (both APIs)")
        UpdateStyledCustomization(
            update = {
                val shape = StyledTheme.shapes.medium
                register(CupertinoLiquidListItem.create())
                update(StyledListItem.DefaultsKey) {
                    copy(shape = shape, minHeight = 64.dp)
                }
            },
        ) {
            StyledListItem(
                headline = { StyledText("Cupertino renderer in the current theme") },
                supporting = { StyledText("Scoped defaults applied after registration") },
            )
            StyledListItem {
                StyledText("Free content uses the same replacement")
            }
        }
    }
}

private val StringSaver: Saver<String, String> = Saver(
    save = { it },
    restore = { it },
)
