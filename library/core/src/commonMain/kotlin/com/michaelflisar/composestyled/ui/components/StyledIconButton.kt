package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.ColorToken
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.classes.asColorToken
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.runtime.LocalStyledContext
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor

object StyledIconButton {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledIconButton")
    val DefaultsKey = StyledKey<Defaults>("StyledIconButtonDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val shape: Shape,
    )

    // -------------
    // 3) customization
    // -------------

    @Immutable
    class Customization internal constructor(
        val colors: CustomColors,
        val border: ColorToken?,
    )

    // -------------
    // 4) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation>, StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key

        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

        @Composable
        fun Render(
            variant: StyledButton.Variant,
            onClick: () -> Unit,
            modifier: Modifier,
            enabled: Boolean,
            shape: Shape,
            interactionSource: MutableInteractionSource?,
            customization: Customization?,
            content: @Composable () -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledIconButtonDefaults {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    fun shape(): Shape =
        if (LocalStyledContext.current is StyledInput.Context) {
            StyledInputDefaults.actionIconButtonShape
        } else {
            LocalStyledRegistry.current[StyledIconButton.DefaultsKey].shape
        }

    fun customize(
        colors: CustomColors,
    ) = StyledIconButton.Customization(colors, null)

    fun customize(
        colors: CustomColors,
        border: Color,
    ) = customize(colors, border.asColorToken())

    fun customize(
        colors: CustomColors,
        border: ColorToken,
    ) = StyledIconButton.Customization(colors, border)
}

// -------------------------
// public API
// -------------------------

/** A theme-rendered icon-only action button; [variant] defaults to a context-aware style. */
@Composable
fun StyledIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    // defaults / customisation
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    // content
    content: @Composable () -> Unit,
) {
    StyledIconButtonImpl(
        variant = variant,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        customization = customization,
        content = content,
    )
}

/**
 * Convenience overload that renders [icon] with its accessibility description and optional padding.
 */
@Composable
fun StyledIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    variant: StyledButton.Variant = StyledButton.Variant.Auto,
    // defaults / customisation
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    // content
    icon: IconComposable,
    contentDescription: String? = null,
    iconSize: Dp? = StyledTheme.sizes.iconSize,
    iconPaddingValues: PaddingValues = PaddingValues(0.dp),
    tint: Color? = null,
) {
    StyledIconButtonImpl(
        variant = variant,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        customization = customization,
        content = {
            StyledIcon(
                icon = icon,
                modifier = Modifier
                    .then(if (iconSize == null) Modifier else Modifier.size(iconSize))
                    .padding(iconPaddingValues),
                contentDescription = contentDescription,
                size = null,
                tint = tint ?: LocalStyledContentColor.current,
            )
        },
    )
}

/** A theme-rendered filled icon button. Its visual variant is fixed. */
@Composable
fun StyledIconFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    content: @Composable () -> Unit,
) {
    StyledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        variant = StyledButton.Variant.Filled,
        shape = shape,
        customization = customization,
        content = content,
    )
}

/** A theme-rendered filled icon button for an [icon] with optional padding. */
@Composable
fun StyledIconFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    icon: IconComposable,
    contentDescription: String? = null,
    iconSize: Dp? = StyledTheme.sizes.iconSize,
    iconPaddingValues: PaddingValues = PaddingValues(0.dp),
    tint: Color? = null,
) {
    StyledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        variant = StyledButton.Variant.Filled,
        shape = shape,
        customization = customization,
        icon = icon,
        contentDescription = contentDescription,
        iconSize = iconSize,
        iconPaddingValues = iconPaddingValues,
        tint = tint,
    )
}

/** A theme-rendered outlined icon button. Its visual variant is fixed. */
@Composable
fun StyledIconOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    content: @Composable () -> Unit,
) {
    StyledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        variant = StyledButton.Variant.Outlined,
        shape = shape,
        customization = customization,
        content = content,
    )
}

/** A theme-rendered outlined icon button for an [icon] with optional padding. */
@Composable
fun StyledIconOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    icon: IconComposable,
    contentDescription: String? = null,
    iconSize: Dp? = StyledTheme.sizes.iconSize,
    iconPaddingValues: PaddingValues = PaddingValues(0.dp),
    tint: Color? = null,
) {
    StyledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        variant = StyledButton.Variant.Outlined,
        shape = shape,
        customization = customization,
        icon = icon,
        contentDescription = contentDescription,
        iconSize = iconSize,
        iconPaddingValues = iconPaddingValues,
        tint = tint,
    )
}

/** A theme-rendered plain icon button. Its visual variant is fixed. */
@Composable
fun StyledIconPlainButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    content: @Composable () -> Unit,
) {
    StyledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        variant = StyledButton.Variant.Plain,
        shape = shape,
        customization = customization,
        content = content,
    )
}

/** A theme-rendered plain icon button for an [icon] with optional padding. */
@Composable
fun StyledIconPlainButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    icon: IconComposable,
    contentDescription: String? = null,
    iconSize: Dp? = StyledTheme.sizes.iconSize,
    iconPaddingValues: PaddingValues = PaddingValues(0.dp),
    tint: Color? = null,
) {
    StyledIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        variant = StyledButton.Variant.Plain,
        shape = shape,
        customization = customization,
        icon = icon,
        contentDescription = contentDescription,
        iconSize = iconSize,
        iconPaddingValues = iconPaddingValues,
        tint = tint,
    )
}

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledIconButtonImpl(
    variant: StyledButton.Variant,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource?,
    shape: Shape,
    customization: StyledIconButton.Customization?,
    content: @Composable () -> Unit,
) {
    val buttonModifier = modifier.withStyledInputPointerIcon()
    val buttonEnabled = enabled && LocalStyledInputEnabled.current
    LocalStyledRegistry.current[StyledIconButton.Key].Render(
        variant = variant,
        onClick = onClick,
        modifier = buttonModifier,
        enabled = buttonEnabled,
        shape = shape,
        interactionSource = interactionSource,
        customization = customization,
        content = content,
    )
}
