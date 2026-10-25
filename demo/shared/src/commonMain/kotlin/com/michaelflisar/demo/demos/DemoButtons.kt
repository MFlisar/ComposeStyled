package com.michaelflisar.demo.demos

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.RowScope
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.classes.CustomColors
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedFlowRow
import com.michaelflisar.composestyledcomponents.ui.components.StyledButton
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.ui.components.StyledIconButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledIconButtonDefaults
import com.michaelflisar.composestyledcomponents.isDark
import com.michaelflisar.composestyledcomponents.ui.components.StyledButtonDefaults
import com.michaelflisar.composestyledcomponents.ui.components.StyledPrimaryButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledSecondaryButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledTertiaryButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledPrimaryIconButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledSecondaryIconButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledTertiaryIconButton
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import com.michaelflisar.demo.helper.ExpandState

@Composable
internal fun DemoButtons(
    baseId: Int,
    expandState: ExpandState,
    showMessage: (String) -> Unit,
) {
    RegionContainer(
        title = "Buttons",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Default Buttons")
        StyledSpacedFlowRow {
            StyledButton.Role.entries.forEach {
                ButtonForRole(
                    role = it,
                    onClick = { showMessage("${it.name} Button clicked") }
                ) {
                    StyledIcon(icon = Icons.Default.Add.toIconComposable(), contentDescription = null)
                    StyledText("${it.name} Button")
                }
            }
        }

        SubTitle("Icon Buttons")
        StyledSpacedFlowRow {
            StyledIconButton.Role.entries.forEach { role ->
                IconButtonForRole(
                    role = role,
                    onClick = { showMessage("${role.name} IconButton clicked") },
                ) {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = "${role.name} action",
                    )
                }
            }
        }
        StyledSpacedFlowRow {
            StyledIconButton.Role.entries.forEach { role ->
                IconButtonForRole(
                    role = role,
                    onClick = {},
                    enabled = false,
                ) {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = null,
                    )
                }
            }
        }
        StyledSpacedFlowRow {
            val customization = StyledIconButtonDefaults.customize(
                colors = CustomColors(Color.Magenta, Color.White, Color.Magenta),
                border = Color.Magenta,
            )
            StyledIconButton.Role.entries.forEach { role ->
                IconButtonForRole(
                    role = role,
                    onClick = { showMessage("${role.name} customized IconButton clicked") },
                    customization = customization,
                ) {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = "${role.name} customized action",
                    )
                }
            }
        }

        SubTitle("Disabled Buttons")
        StyledSpacedFlowRow {
            StyledButton.Role.entries.forEach {
                ButtonForRole(
                    role = it,
                    onClick = {},
                    enabled = false
                ) {
                    StyledText("${it.name} Button")
                }
            }
        }

        SubTitle("Buttons with Customization")
        StyledSpacedFlowRow {
            val colors = listOf(
                Color.Red,
                Color.Green,
                Color.Magenta,
                Color.Blue,
                Color.Cyan
            )
            StyledButton.Role.entries.forEachIndexed { index, role ->
                val color = colors[index % colors.size]
                val onColor = if (color.isDark) Color.White else Color.Black
                val customization = StyledButtonDefaults.customize(
                    colors = CustomColors(
                        filledBackground = color,
                        filledForeground = onColor,
                        textForeground = color,
                    ),
                    border = color
                )
                ButtonForRole(
                    role = role,
                    onClick = { showMessage("${role.name} Button clicked") },
                    customization = customization
                ) {
                    StyledText("${role.name} Button")
                }
            }

        }
    }
}

@Composable
private fun ButtonForRole(
    role: StyledButton.Role,
    onClick: () -> Unit,
    enabled: Boolean = true,
    customization: StyledButton.Customization? = null,
    content: @Composable RowScope.() -> Unit,
) {
    when (role) {
        StyledButton.Role.Primary -> StyledPrimaryButton(onClick, enabled = enabled, customization = customization, content = content)
        StyledButton.Role.Secondary -> StyledSecondaryButton(onClick, enabled = enabled, customization = customization, content = content)
        StyledButton.Role.Tertiary -> StyledTertiaryButton(onClick, enabled = enabled, customization = customization, content = content)
    }
}

@Composable
private fun IconButtonForRole(
    role: StyledIconButton.Role,
    onClick: () -> Unit,
    enabled: Boolean = true,
    customization: StyledIconButton.Customization? = null,
    content: @Composable () -> Unit,
) {
    when (role) {
        StyledIconButton.Role.Primary -> StyledPrimaryIconButton(onClick, enabled = enabled, customization = customization, content = content)
        StyledIconButton.Role.Secondary -> StyledSecondaryIconButton(onClick, enabled = enabled, customization = customization, content = content)
        StyledIconButton.Role.Tertiary -> StyledTertiaryIconButton(onClick, enabled = enabled, customization = customization, content = content)
    }
}
