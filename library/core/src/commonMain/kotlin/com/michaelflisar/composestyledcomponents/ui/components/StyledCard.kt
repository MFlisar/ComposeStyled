package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyledcomponents.classes.ColorToken
import com.michaelflisar.composestyledcomponents.classes.CustomColors
import com.michaelflisar.composestyledcomponents.classes.asColorToken
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledCard {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledCard")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(
        val shape: Shape,
        val contentPadding: PaddingValues,
    )

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Variant {
        Standard,
        Alternative,
    }

    // -------------
    // 4) customization
    // -------------

    @Immutable
    class Customization internal constructor(
        val colors: CustomColors,
        val border: ColorToken?,
        val elevation: Dp?,
    )

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        val defaults: Defaults

        @Composable
        fun Render(
            variant: Variant,
            modifier: Modifier,
            shape: Shape,
            customization: Customization?,
            onClick: (() -> Unit)?,
            enabled: Boolean,
            interactionSource: MutableInteractionSource?,
            content: @Composable () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledCardDefaults {
    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledCard.Key].defaults.shape

    val contentPadding: PaddingValues
        @Composable get() = LocalStyledRegistry.current[StyledCard.Key].defaults.contentPadding

    fun customize(
        colors: CustomColors,
        elevation: Dp? = null,
    ) = StyledCard.Customization(colors, null, elevation)

    fun customize(
        colors: CustomColors,
        border: Color,
        elevation: Dp? = null,
    ) = customize(colors, border.asColorToken(), elevation)

    fun customize(
        colors: CustomColors,
        border: ColorToken,
        elevation: Dp? = null,
    ) = StyledCard.Customization(colors, border, elevation)

}

// -------------------------
// public API
// -------------------------

/** Theme-rendered card with optional click handling and Styled content padding. */
@Composable
fun StyledCard(
    modifier: Modifier = Modifier,
    shape: Shape = StyledCardDefaults.shape,
    contentPadding: PaddingValues = StyledCardDefaults.contentPadding,
    customization: StyledCard.Customization? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    applyContentPadding: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    StyledCardImpl(
        variant = StyledCard.Variant.Standard,
        modifier = modifier,
        shape = shape,
        contentPadding = contentPadding,
        customization = customization,
        onClick = onClick,
        enabled = enabled,
        applyContentPadding = applyContentPadding,
        interactionSource = interactionSource,
        content = content,
    )
}

@Composable
fun StyledAlternativeCard(
    modifier: Modifier = Modifier,
    shape: Shape = StyledCardDefaults.shape,
    contentPadding: PaddingValues = StyledCardDefaults.contentPadding,
    customization: StyledCard.Customization? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    applyContentPadding: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable () -> Unit,
) {
    StyledCardImpl(
        variant = StyledCard.Variant.Alternative,
        modifier = modifier,
        shape = shape,
        contentPadding = contentPadding,
        customization = customization,
        onClick = onClick,
        enabled = enabled,
        applyContentPadding = applyContentPadding,
        interactionSource = interactionSource,
        content = content,
    )
}

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledCardImpl(
    variant: StyledCard.Variant,
    modifier: Modifier,
    shape: Shape,
    contentPadding: PaddingValues,
    customization: StyledCard.Customization?,
    onClick: (() -> Unit)?,
    enabled: Boolean,
    applyContentPadding: Boolean,
    interactionSource: MutableInteractionSource?,
    content: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledCard.Key].Render(
        variant = variant,
        modifier = modifier,
        shape = shape,
        customization = customization,
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
    ) {
        Box(
            modifier = if (applyContentPadding) Modifier.padding(contentPadding)
                else Modifier,
            propagateMinConstraints = true,
        ) {
            content()
        }
    }
}
