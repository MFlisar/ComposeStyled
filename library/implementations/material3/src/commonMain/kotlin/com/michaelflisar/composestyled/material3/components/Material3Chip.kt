package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.ui.components.StyledChip
import com.michaelflisar.composestyled.ui.components.StyledIcon

class Material3Chip internal constructor(
    val variantMapping: VariantMapping,
) : StyledChip.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        /**
         * Creates a Material 3 chip renderer with the given mapping.
         */
        @Composable
        fun create(
            variantMapping: VariantMapping = DefaultVariantMapping,
        ) = remember(variantMapping) {
            Material3Chip(variantMapping)
        }

        // -------------
        // 2) mapping
        // -------------

        typealias VariantMapping = (StyledChip.Variant, Boolean) -> Variant

        // non elevated chips have ui bug in m3:
        // if not selected they show a background behind the text when hovered
        // => although on android may not be a real problem
        // => but still android has desktop mode as well...
        val DefaultVariantMapping: VariantMapping = { variant, selectable ->
            when (variant) {
                StyledChip.Variant.Auto,
                StyledChip.Variant.Outlined -> if (selectable) {
                    Variant.ElevatedFilter
                } else {
                    Variant.ElevatedAssist
                }

                StyledChip.Variant.Filled -> if (selectable) {
                    Variant.FilledFilter
                } else {
                    Variant.FilledAssist
                }
            }
        }
    }

    enum class Variant {
        Assist,
        Filter,
        ElevatedAssist,
        ElevatedFilter,
        FilledAssist,
        FilledFilter,

    }

    @Composable
    override fun Render(
        variant: StyledChip.Variant,
        selectable: Boolean,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        interactionSource: MutableInteractionSource?,
        customization: StyledChip.Customization?,
        leadingIcon: IconComposable?,
        trailingIcon: IconComposable?,
        content: @Composable () -> Unit,
    ) {
        val mappedVariant = variantMapping(variant, selectable)
        val filledAssist = mappedVariant == Variant.FilledAssist
        val filledFilter = mappedVariant == Variant.FilledFilter
        val containerColor = customization?.container?.resolve()
            ?: when {
                filledAssist -> StyledTheme.colorScheme.secondarySurface
                filledFilter -> StyledTheme.colorScheme.surfaceVariant
                else -> Color.Unspecified
            }
        val contentColor = customization?.content?.resolve()
            ?.takeUnless { it == Color.Unspecified }
            ?: when {
                filledAssist -> StyledTheme.colorScheme.onSecondarySurface
                filledFilter -> StyledTheme.colorScheme.onSurfaceVariant
                else -> Color.Unspecified
            }
        val borderColor = customization?.border?.resolve() ?: Color.Unspecified
        val selectedContainerColor = customization?.selectedContainer?.resolve()
            ?: if (filledFilter) StyledTheme.colorScheme.secondarySurface else Color.Unspecified
        val selectedContentColor = customization?.selectedContent?.resolve()
            ?.takeUnless { it == Color.Unspecified }
            ?: StyledTheme.colorScheme.onSecondarySurface
        val selectedBorderColor = customization?.selectedBorder?.resolve() ?: Color.Unspecified
        val resolvedContentColor = if (selected) {
            selectedContentColor
        } else {
            contentColor.takeUnless { it == Color.Unspecified } ?: StyledTheme.colorScheme.onSurface
        }
        val defaultIconColor = when {
            filledAssist -> StyledTheme.colorScheme.onSecondarySurface
            mappedVariant == Variant.Assist || mappedVariant == Variant.ElevatedAssist ->
                StyledTheme.colorScheme.onSurfaceVariant
            else -> StyledTheme.colorScheme.onSurfaceVariant
        }
        val resolvedIconColor = if (selected) {
            selectedContentColor
        } else {
            contentColor.takeUnless { it == Color.Unspecified } ?: defaultIconColor
        }
        val styledSlotContentColor = styledChipSlotColor(resolvedContentColor, enabled)
        val styledIconContentColor = styledChipSlotColor(resolvedIconColor, enabled)
        val styledLeadingIconColor = if (
            contentColor == Color.Unspecified &&
            !selected &&
            (mappedVariant == Variant.Assist || mappedVariant == Variant.ElevatedAssist)
        ) {
            styledChipSlotColor(StyledTheme.colorScheme.primary, enabled)
        } else {
            styledIconContentColor
        }
        val label: @Composable () -> Unit = {
            ProvideStyledLocals(
                contentColor = styledSlotContentColor,
                textStyle = StyledTheme.typography.labelLarge,
            ) {
                content()
            }
        }
        val leadingIconSlot: (@Composable () -> Unit)? = leadingIcon?.let { icon ->
            { ChipIcon(icon, 18.dp, styledLeadingIconColor) }
        }
        val trailingIconSlot: (@Composable () -> Unit)? = trailingIcon?.let { icon ->
            { ChipIcon(icon, 18.dp, styledIconContentColor) }
        }

        when (mappedVariant) {
            Variant.Assist -> {
                val border = customization?.border?.resolve()?.let {
                    AssistChipDefaults.assistChipBorder(
                        enabled = enabled,
                        borderColor = it,
                    )
                } ?: AssistChipDefaults.assistChipBorder(enabled)
                AssistChip(
                    onClick = onClick,
                    label = label,
                    modifier = modifier,
                    enabled = enabled,
                    leadingIcon = leadingIconSlot,
                    trailingIcon = trailingIconSlot,
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = contentColor,
                        leadingIconContentColor = contentColor,
                        trailingIconContentColor = contentColor,
                    ),
                    border = border,
                    interactionSource = interactionSource,
                )
            }

            Variant.ElevatedAssist -> {
                val border = customization?.border?.resolve()?.let {
                    AssistChipDefaults.assistChipBorder(
                        enabled = enabled,
                        borderColor = it,
                    )
                }
                ElevatedAssistChip(
                    onClick = onClick,
                    label = label,
                    modifier = modifier,
                    enabled = enabled,
                    leadingIcon = leadingIconSlot,
                    trailingIcon = trailingIconSlot,
                    colors = AssistChipDefaults.elevatedAssistChipColors(
                        containerColor = containerColor,
                        labelColor = contentColor,
                        leadingIconContentColor = contentColor,
                        trailingIconContentColor = contentColor,
                    ),
                    border = border,
                    interactionSource = interactionSource,
                )
            }

            Variant.FilledAssist -> {
                val border = customization?.border?.resolve()?.let {
                    AssistChipDefaults.assistChipBorder(
                        enabled = enabled,
                        borderColor = it,
                    )
                }
                AssistChip(
                    onClick = onClick,
                    label = label,
                    modifier = modifier,
                    enabled = enabled,
                    leadingIcon = leadingIconSlot,
                    trailingIcon = trailingIconSlot,
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = containerColor,
                        labelColor = contentColor,
                        leadingIconContentColor = contentColor,
                        trailingIconContentColor = contentColor,
                    ),
                    border = border,
                    interactionSource = interactionSource,
                )
            }

            Variant.Filter -> {
                val border = if (customization?.border != null || customization?.selectedBorder != null) {
                    FilterChipDefaults.filterChipBorder(
                        enabled = enabled,
                        selected = selected,
                        borderColor = borderColor,
                        selectedBorderColor = selectedBorderColor,
                    )
                } else {
                    FilterChipDefaults.filterChipBorder(enabled, selected)
                }
                FilterChip(
                    selected = selected,
                    onClick = onClick,
                    label = label,
                    modifier = modifier,
                    enabled = enabled,
                    leadingIcon = leadingIconSlot,
                    trailingIcon = trailingIconSlot,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = containerColor,
                        labelColor = contentColor,
                        iconColor = contentColor,
                        selectedContainerColor = selectedContainerColor,
                        selectedLabelColor = selectedContentColor,
                        selectedLeadingIconColor = selectedContentColor,
                        selectedTrailingIconColor = selectedContentColor,
                    ),
                    border = border,
                    interactionSource = interactionSource,
                )
            }

            Variant.ElevatedFilter -> {
                val border = if (customization?.border != null || customization?.selectedBorder != null) {
                    FilterChipDefaults.filterChipBorder(
                        enabled = enabled,
                        selected = selected,
                        borderColor = borderColor,
                        selectedBorderColor = selectedBorderColor,
                    )
                } else {
                    null
                }
                ElevatedFilterChip(
                    selected = selected,
                    onClick = onClick,
                    label = label,
                    modifier = modifier,
                    enabled = enabled,
                    leadingIcon = leadingIconSlot,
                    trailingIcon = trailingIconSlot,
                    colors = FilterChipDefaults.elevatedFilterChipColors(
                        containerColor = containerColor,
                        labelColor = contentColor,
                        iconColor = contentColor,
                        selectedContainerColor = selectedContainerColor,
                        selectedLabelColor = selectedContentColor,
                        selectedLeadingIconColor = selectedContentColor,
                        selectedTrailingIconColor = selectedContentColor,
                    ),
                    border = border,
                    interactionSource = interactionSource,
                )
            }

            Variant.FilledFilter -> {
                val border = if (customization?.border != null || customization?.selectedBorder != null) {
                    FilterChipDefaults.filterChipBorder(
                        enabled = enabled,
                        selected = selected,
                        borderColor = borderColor,
                        selectedBorderColor = selectedBorderColor,
                    )
                } else {
                    null
                }
                FilterChip(
                    selected = selected,
                    onClick = onClick,
                    label = label,
                    modifier = modifier,
                    enabled = enabled,
                    leadingIcon = leadingIconSlot,
                    trailingIcon = trailingIconSlot,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = containerColor,
                        labelColor = contentColor,
                        iconColor = contentColor,
                        selectedContainerColor = selectedContainerColor,
                        selectedLabelColor = selectedContentColor,
                        selectedLeadingIconColor = selectedContentColor,
                        selectedTrailingIconColor = selectedContentColor,
                    ),
                    border = border,
                    interactionSource = interactionSource,
                )
            }
        }
    }
}

@Composable
private fun styledChipSlotColor(color: Color, enabled: Boolean) =
    if (enabled) color else color.copy(
        alpha = color.alpha * StyledTheme.colorScheme.disableFactors.content,
    )

@Composable
private fun ChipIcon(
    icon: IconComposable,
    size: androidx.compose.ui.unit.Dp,
    contentColor: Color,
) {
    ProvideStyledLocals(contentColor = contentColor) {
        StyledIcon(
            icon = icon,
            size = size,
        )
    }
}
