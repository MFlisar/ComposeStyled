package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.classes.ColorToken
import com.michaelflisar.composestyledcomponents.classes.asColorToken
import com.michaelflisar.composestyledcomponents.classes.IconComposable
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledChip {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledChip")

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Variant {
        NonSelectable,
        Selectable,
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
        container = container.asTokenOrNull(),
        content = content.asTokenOrNull(),
        border = border.asTokenOrNull(),
        selectedContainer = selectedContainer.asTokenOrNull(),
        selectedContent = selectedContent.asTokenOrNull(),
        selectedBorder = selectedBorder.asTokenOrNull(),
    )

    private fun Color.asTokenOrNull(): ColorToken? =
        takeIf { it != Color.Unspecified }?.asColorToken()
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledChip(
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
) = StyledChipImpl(
    variant = StyledChip.Variant.NonSelectable,
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

@Composable
fun StyledSelectableChip(
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
) = StyledChipImpl(
    variant = StyledChip.Variant.Selectable,
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

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledChipImpl(
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
    LocalStyledRegistry.current[StyledChip.Key].Render(
        variant = variant,
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
