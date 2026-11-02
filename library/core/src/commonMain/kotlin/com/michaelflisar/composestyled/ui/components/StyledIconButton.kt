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
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.classes.ColorToken
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
    // 3) roles / variants / context
    // -------------

    enum class Role {
        /** Lets the active theme choose a style that fits the current context. */
        Default,
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

    interface Implementation : StyledComponent<Implementation>, StyledComponentWithDefaults<Defaults> {
        override val key: StyledKey<Implementation>
            get() = Key

        override val defaultsKey: StyledKey<Defaults>
            get() = DefaultsKey

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

@Composable
fun StyledIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    role: StyledIconButton.Role = StyledIconButton.Role.Default,
    // defaults / customisation
    shape: Shape = StyledIconButtonDefaults.shape(),
    customization: StyledIconButton.Customization? = null,
    // content
    content: @Composable () -> Unit,
) {
    StyledIconButtonImpl(
        role = role,
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
fun StyledIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    role: StyledIconButton.Role = StyledIconButton.Role.Default,
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
        role = role,
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
