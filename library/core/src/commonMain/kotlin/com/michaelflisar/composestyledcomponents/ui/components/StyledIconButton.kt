package com.michaelflisar.composestyledcomponents.ui.components

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
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.ColorToken
import com.michaelflisar.composestyledcomponents.classes.CustomColors
import com.michaelflisar.composestyledcomponents.classes.IconComposable
import com.michaelflisar.composestyledcomponents.classes.asColorToken
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.runtime.LocalStyledContext
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledContentColor
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey

object StyledIconButton {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledIconButton")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(
        val shape: Shape,
    )

    // -------------
    // 3) roles / variants / context
    // -------------

    enum class Role {
        Primary,
        Secondary,
        Tertiary,
    }

    // -------------
    // 4) customization
    // -------------

    @Immutable
    class Customization internal constructor(
        val colors: CustomColors,
        val border: ColorToken?,
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
            role: Role,
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
            LocalStyledRegistry.current[StyledIconButton.Key].defaults.shape
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

@Composable
fun StyledPrimaryIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    // content
    content: @Composable () -> Unit,
) {
    StyledIconButtonImpl(
        role = StyledIconButton.Role.Primary,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        customization = customization,
        content = content,
    )
}

@Composable
fun StyledPrimaryIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
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
        role = StyledIconButton.Role.Primary,
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

@Composable
fun StyledSecondaryIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    // content
    content: @Composable () -> Unit,
) = StyledIconButtonImpl(
    role = StyledIconButton.Role.Secondary,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    shape = shape,
    customization = customization,
    content = content,
)

@Composable
fun StyledSecondaryIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    // content
    icon: IconComposable,
    contentDescription: String? = null,
    iconSize: Dp? = StyledTheme.sizes.iconSize,
    iconPaddingValues: PaddingValues = PaddingValues(0.dp),
    tint: Color? = null,
) = StyledIconButtonImpl(
    role = StyledIconButton.Role.Secondary,
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

@Composable
fun StyledTertiaryIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    // content
    content: @Composable () -> Unit,
) = StyledIconButtonImpl(
    role = StyledIconButton.Role.Tertiary,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    interactionSource = interactionSource,
    shape = shape,
    customization = customization,
    content = content,
)


@Composable
fun StyledTertiaryIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    // content
    icon: IconComposable,
    contentDescription: String? = null,
    iconSize: Dp? = StyledTheme.sizes.iconSize,
    iconPaddingValues: PaddingValues = PaddingValues(0.dp),
    tint: Color? = null,
) = StyledIconButtonImpl(
    role = StyledIconButton.Role.Tertiary,
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

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledIconButtonImpl(
    role: StyledIconButton.Role,
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
        role = role,
        onClick = onClick,
        modifier = buttonModifier,
        enabled = buttonEnabled,
        shape = shape,
        interactionSource = interactionSource,
        customization = customization,
        content = content,
    )
}
