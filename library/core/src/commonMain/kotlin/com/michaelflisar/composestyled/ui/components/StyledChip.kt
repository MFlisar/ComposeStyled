package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.classes.ColorToken
import com.michaelflisar.composestyled.classes.asColorTokenOrNull
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey

object StyledChip {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledChip")

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Variant {
        /** Lets the active theme choose its default treatment. */
        Auto,
        Filled,
        Outlined,
    }

    // -------------
    // 4) customization
    // -------------

    @Immutable
    class Customization internal constructor(
        val container: ColorToken?,
        val content: ColorToken?,
        val border: ColorToken?,
        val selectedContainer: ColorToken?,
        val selectedContent: ColorToken?,
        val selectedBorder: ColorToken?,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            variant: Variant,
            selectable: Boolean,
            selected: Boolean,
            onClick: () -> Unit,
            modifier: Modifier,
            enabled: Boolean,
            interactionSource: MutableInteractionSource?,
            customization: Customization?,
            leadingIcon: IconComposable?,
            trailingIcon: IconComposable?,
            content: @Composable () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledChipDefaults {

    fun colors(
        container: Color = Color.Unspecified,
        content: Color = Color.Unspecified,
        border: Color = Color.Unspecified,
        selectedContainer: Color = Color.Unspecified,
        selectedContent: Color = Color.Unspecified,
        selectedBorder: Color = Color.Unspecified,
    ) = StyledChip.Customization(
        container = container.asColorTokenOrNull(),
        content = content.asColorTokenOrNull(),
        border = border.asColorTokenOrNull(),
        selectedContainer = selectedContainer.asColorTokenOrNull(),
        selectedContent = selectedContent.asColorTokenOrNull(),
        selectedBorder = selectedBorder.asColorTokenOrNull(),
    )
}

// -------------------------
// public API
// -------------------------

/** A theme-rendered clickable chip for an action that does not represent persistent selection. */
@Composable
fun StyledChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    variant: StyledChip.Variant = StyledChip.Variant.Auto,
    // defaults / customisation
    customization: StyledChip.Customization? = null,
    // content
    leadingIcon: IconComposable? = null,
    trailingIcon: IconComposable? = null,
    content: @Composable () -> Unit,
) = StyledChipImpl(
    variant = variant,
    selectable = false,
    selected = false,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    customization = customization,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
    content = content,
)

/** An outlined-style variant of [StyledChip]. */
@Composable
fun StyledOutlinedChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    customization: StyledChip.Customization? = null,
    // content
    leadingIcon: IconComposable? = null,
    trailingIcon: IconComposable? = null,
    content: @Composable () -> Unit,
) = StyledChip(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    variant = StyledChip.Variant.Outlined,
    customization = customization,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
    content = content,
)

/** A clickable chip whose appearance reflects [selected]. */
@Composable
fun StyledSelectableChip(
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    variant: StyledChip.Variant = StyledChip.Variant.Auto,
    // defaults / customisation
    customization: StyledChip.Customization? = null,
    // content
    leadingIcon: IconComposable? = null,
    trailingIcon: IconComposable? = null,
    content: @Composable () -> Unit,
) = StyledChipImpl(
    variant = variant,
    selectable = true,
    selected = selected,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    customization = customization,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
    content = content,
)

/** An outlined-style variant of [StyledSelectableChip]. */
@Composable
fun StyledOutlinedSelectableChip(
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    customization: StyledChip.Customization? = null,
    // content
    leadingIcon: IconComposable? = null,
    trailingIcon: IconComposable? = null,
    content: @Composable () -> Unit,
) = StyledSelectableChip(
    onClick = onClick,
    selected = selected,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    variant = StyledChip.Variant.Outlined,
    customization = customization,
    leadingIcon = leadingIcon,
    trailingIcon = trailingIcon,
    content = content,
)

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledChipImpl(
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
    LocalStyledRegistry.current[StyledChip.Key].Render(
        variant = variant,
        selectable = selectable,
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        customization = customization,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        content = content,
    )
}
