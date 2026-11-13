package com.michaelflisar.demo.demos.showcase.areas

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
import com.michaelflisar.composestyled.ui.components.StyledIconPlainButton
import com.michaelflisar.composestyled.ui.components.StyledOutlinedButton
import com.michaelflisar.composestyled.ui.components.StyledSurface
import com.michaelflisar.composestyled.ui.components.StyledTextButton
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
            StyledButton.Variant.entries.forEach {
                ButtonForVariant(
                    variant = it,
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
            StyledButton.Variant.entries.forEach { variant ->
                IconButtonForVariant(
                    variant = variant,
                    onClick = { showMessage("${variant.name} IconButton clicked") },
                ) {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = "${variant.name} action",
                    )
                }
            }
        }
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledButton.Variant.entries.forEach { variant ->
                IconButtonForVariant(
                    variant = variant,
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
            StyledButton.Variant.entries.forEach { variant ->
                IconButtonForVariant(
                    variant = variant,
                    onClick = { showMessage("${variant.name} customized IconButton clicked") },
                    customization = customization,
                ) {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = "${variant.name} customized action",
                    )
                }
            }
        }

        SubTitle("Disabled Buttons")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledButton.Variant.entries.forEach {
                ButtonForVariant(
                    variant = it,
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
            StyledButton.Variant.entries.forEachIndexed { index, variant ->
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
                ButtonForVariant(
                    variant = variant,
                    onClick = { showMessage("${variant.name} Button clicked") },
                    customization = customization
                ) {
                    StyledText("${variant.name} Button")
                }
            }

        }

        SubTitle("Buttons with Leading and Trailing Icons")
        StyledFlowRow(spacing = StyledTheme.spacing.medium) {
            StyledButton(
                onClick = { showMessage("Icon button clicked") },
                leadingIcon = Icons.Default.Add.toIconComposable(),
                trailingIcon = Icons.Default.Add.toIconComposable(),
            ) {
                StyledText("Button")
            }
            StyledOutlinedButton(
                onClick = { showMessage("Outlined button clicked") },
                leadingIcon = Icons.Default.Add.toIconComposable(),
            ) {
                StyledText("Outlined")
            }
            StyledTextButton(onClick = { showMessage("Text button clicked") }) {
                StyledText("Text")
            }
        }

        SubTitle("Plain buttons inherit surface content color")
        StyledSurface(
            color = StyledTheme.colorScheme.primary,
            contentColor = StyledTheme.colorScheme.onPrimary,
        ) {
            StyledFlowRow(spacing = StyledTheme.spacing.medium) {
                StyledTextButton(
                    onClick = { showMessage("Plain icon-text button clicked") },
                    leadingIcon = Icons.Default.Add.toIconComposable(),
                ) {
                    StyledText("Plain button")
                }
                StyledIconPlainButton(
                    onClick = { showMessage("Plain icon button clicked") },
                    icon = Icons.Default.Add.toIconComposable(),
                    contentDescription = "Plain action",
                )
            }
        }
    }
}

@Composable
private fun ButtonForVariant(
    variant: StyledButton.Variant,
    onClick: () -> Unit,
    enabled: Boolean = true,
    customization: StyledButton.Customization? = null,
    content: @Composable RowScope.() -> Unit,
) {
    StyledButton(
        onClick = onClick,
        variant = variant,
        enabled = enabled,
        customization = customization,
        content = content,
    )
}

@Composable
private fun IconButtonForVariant(
    variant: StyledButton.Variant,
    onClick: () -> Unit,
    enabled: Boolean = true,
    customization: StyledIconButton.Customization? = null,
    content: @Composable () -> Unit,
) {
    StyledIconButton(
        onClick = onClick,
        variant = variant,
        enabled = enabled,
        customization = customization,
        content = content,
    )
}
