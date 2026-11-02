package com.michaelflisar.demo.demos.regions

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.onColorByContrastRatio
import com.michaelflisar.composestyled.ui.components.StyledButton
import com.michaelflisar.composestyled.ui.components.StyledButtonDefaults
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledIconButtonDefaults
import com.michaelflisar.composestyled.ui.layout.StyledFlowRow
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

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
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledButton.Role.entries.forEach {
                ButtonForRole(
                    role = it,
                    onClick = { showMessage("${it.name} Button clicked") }
                ) {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = null
                    )
                    StyledText("${it.name} Button")
                }
            }
        }

        SubTitle("Icon Buttons")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
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
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
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
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
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
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
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
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            val colors = listOf(
                Color.Red,
                Color.Green,
                Color.Magenta,
                Color.Blue,
                Color.Cyan
            )
            StyledButton.Role.entries.forEachIndexed { index, role ->
                val color = colors[index % colors.size]
                val onColor = color.onColorByContrastRatio()
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
    StyledButton(
        onClick = onClick,
        role = role,
        enabled = enabled,
        customization = customization,
        content = content,
    )
}

@Composable
private fun IconButtonForRole(
    role: StyledIconButton.Role,
    onClick: () -> Unit,
    enabled: Boolean = true,
    customization: StyledIconButton.Customization? = null,
    content: @Composable () -> Unit,
) {
    StyledIconButton(
        onClick = onClick,
        role = role,
        enabled = enabled,
        customization = customization,
        content = content,
    )
}
