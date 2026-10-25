package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.classes.IconComposable
import com.michaelflisar.composestyledcomponents.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyledcomponents.ui.components.StyledChip

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

        typealias VariantMapping = (StyledChip.Variant) -> Variant

        // non elevated chips have ui bug in m3:
        // if not selected they show a background behind the text when hovered
        // => although on android may not be a real problem
        // => but still android has desktop mode as well...
        val DefaultVariantMapping: VariantMapping = { variant ->
            when (variant) {
                StyledChip.Variant.NonSelectable -> Variant.ElevatedAssist
                StyledChip.Variant.Selectable -> Variant.ElevatedFilter
            }
        }
    }

    enum class Variant {
        Assist,
        Filter,
        ElevatedAssist,
        ElevatedFilter,

    }

    @Composable
    override fun Render(
        variant: StyledChip.Variant,
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
        val containerColor = customization?.container?.resolve() ?: Color.Unspecified
        val contentColor = customization?.content?.resolve() ?: Color.Unspecified
        val borderColor = customization?.border?.resolve() ?: Color.Unspecified
        val selectedContainerColor = customization?.selectedContainer?.resolve() ?: Color.Unspecified
        val selectedContentColor = customization?.selectedContent?.resolve() ?: Color.Unspecified
        val selectedBorderColor = customization?.selectedBorder?.resolve() ?: Color.Unspecified
        val label: @Composable () -> Unit = {
            CopyMaterialThemeLocals(contentColor = LocalContentColor.current) {
                content()
            }
        }
        val leadingIconSlot: (@Composable () -> Unit)? = leadingIcon?.let { icon ->
            { ChipIcon(icon, 18.dp) }
        }
        val trailingIconSlot: (@Composable () -> Unit)? = trailingIcon?.let { icon ->
            { ChipIcon(icon, 18.dp) }
        }

        when (variantMapping(variant)) {
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
        }
    }
}

@Composable
private fun ChipIcon(
    icon: IconComposable,
    size: androidx.compose.ui.unit.Dp,
) {
    CopyMaterialThemeLocals(contentColor = LocalContentColor.current) {
        IconComposable(
            icon = icon,
            modifier = Modifier.size(size),
        )
    }
}
