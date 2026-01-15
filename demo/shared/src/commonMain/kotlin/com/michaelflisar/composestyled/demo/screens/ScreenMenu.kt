package com.michaelflisar.composestyled.demo.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.components.layout.ContentColumn
import com.michaelflisar.composestyled.components.menu.Menu
import com.michaelflisar.composestyled.components.menu.MenuItem
import com.michaelflisar.composestyled.components.menu.MenuSubMenu
import com.michaelflisar.composestyled.components.menu.PopupMenu
import com.michaelflisar.composestyled.components.menu.PopupMenuBox
import com.michaelflisar.composestyled.components.menu.rememberMenuSetup
import com.michaelflisar.composestyled.components.menu.rememberMenuState
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledText

@Composable
fun ScreenMenu() {
    ContentColumn(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        StyledText("Menu", style = StyledTheme.typography.titleMedium)
        val menu = rememberMenuState()
        PopupMenuBox(
            content = {
                StyledButton(
                    onClick = { menu.show() }
                ) {
                    StyledText("Open Menu Demo (not implemented yet)")
                }
            },
            menu = {
                PopupMenu(
                    state = menu,
                    setup = rememberMenuSetup(offset = DpOffset(x = 0.dp, y = StyledTheme.spacings.small))
                ) {
                    MenuItem(text = { StyledText("Item 1") })
                    MenuItem(text = { StyledText("Item 2") })
                    MenuItem(text = { StyledText("Item 3") })
                    MenuSubMenu(text = { StyledText("Item 4")}) {
                        MenuItem(text = { StyledText("Item 4.1") })
                        MenuItem(text = { StyledText("Item 4.2") })
                        MenuSubMenu(text = { StyledText("Item 4.3")}) {
                            MenuItem(text = { StyledText("Item 4.3.1") })
                            MenuItem(text = { StyledText("Item 4.3.2") })
                        }
                    }
                    MenuItem(text = { StyledText("Item 5") })
                }
            }
        )
    }
}