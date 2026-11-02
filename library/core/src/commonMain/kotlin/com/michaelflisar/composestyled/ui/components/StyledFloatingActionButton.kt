package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey

object StyledFloatingActionButton {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledFloatingActionButton")
        val DefaultsKey = StyledKey<Defaults>("StyledFloatingActionButtonDefaults")
    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(val shape: Shape)

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Variant { Small, Standard, Large }

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation>, StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key

        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

        @Composable
        fun Render(
            onClick: () -> Unit,
            modifier: Modifier,
            variant: Variant,
            shape: Shape,
            containerColor: Color,
            contentColor: Color,
            interactionSource: MutableInteractionSource?,
            content: @Composable () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledFloatingActionButtonDefaults {
    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledFloatingActionButton.DefaultsKey].shape
}

// -------------------------
// public API
// -------------------------

@Composable
fun StyledFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledFloatingActionButtonDefaults.shape,
    containerColor: Color = StyledTheme.colorScheme.primarySurface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    // content
    content: @Composable () -> Unit,
) {
    StyledFloatingActionButtonImpl(
        variant = StyledFloatingActionButton.Variant.Standard,
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}

@Composable
fun StyledSmallFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledFloatingActionButtonDefaults.shape,
    containerColor: Color = StyledTheme.colorScheme.primarySurface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    // content
    content: @Composable () -> Unit,
) = StyledFloatingActionButtonImpl(
    variant = StyledFloatingActionButton.Variant.Small,
    onClick = onClick,
    modifier = modifier,
    interactionSource = interactionSource,
    shape = shape,
    containerColor = containerColor,
    contentColor = contentColor,
    content = content,
)

@Composable
fun StyledLargeFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledFloatingActionButtonDefaults.shape,
    containerColor: Color = StyledTheme.colorScheme.primarySurface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    // content
    content: @Composable () -> Unit,
) = StyledFloatingActionButtonImpl(
    variant = StyledFloatingActionButton.Variant.Large,
    onClick = onClick,
    modifier = modifier,
    interactionSource = interactionSource,
    shape = shape,
    containerColor = containerColor,
    contentColor = contentColor,
    content = content,
)

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledFloatingActionButtonImpl(
    variant: StyledFloatingActionButton.Variant,
    onClick: () -> Unit,
    modifier: Modifier,
    interactionSource: MutableInteractionSource?,
    shape: Shape,
    containerColor: Color,
    contentColor: Color,
    content: @Composable () -> Unit,
) {
    LocalStyledRegistry.current[StyledFloatingActionButton.Key].Render(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        interactionSource = interactionSource,
        content = content,
    )
}
