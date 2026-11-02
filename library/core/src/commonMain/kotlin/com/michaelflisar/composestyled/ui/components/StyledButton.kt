package com.michaelflisar.composestyled.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.classes.ColorToken
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.classes.asColorToken
import com.michaelflisar.composestyled.registry.LocalStyledRegistry
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledComponentWithDefaults
import com.michaelflisar.composestyled.registry.StyledKey
import com.michaelflisar.composestyled.ui.components.StyledButton.Customization

object StyledButton {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledButton")
    val DefaultsKey = StyledKey<Defaults>("StyledButtonDefaults")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    data class Defaults(
        val shape: Shape,
    )

    // -------------
    // 3) roles / variants
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
            content: @Composable RowScope.() -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledButtonDefaults {

    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledButton.DefaultsKey].shape

    fun customize(
        colors: CustomColors
    ) = Customization(colors, null)

    fun customize(
        colors: CustomColors,
        border: Color,
    ) = customize(colors, border.asColorToken())

    fun customize(
        colors: CustomColors,
        border: ColorToken,
    ) = Customization(colors, border)

}

// -------------------------
// public API
// -------------------------

/** A theme-rendered action button; [role] lets the active theme choose its visual emphasis. */
@Composable
fun StyledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    role: StyledButton.Role = StyledButton.Role.Default,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) {
    StyledButtonImpl(
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

// -------------------------
// private API
// -------------------------

@Composable
private fun StyledButtonImpl(
    role: StyledButton.Role,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    interactionSource: MutableInteractionSource?,
    shape: Shape,
    customization: Customization?,
    content: @Composable RowScope.() -> Unit,
) {
    val buttonModifier = modifier.withStyledInputPointerIcon()
    LocalStyledRegistry.current[StyledButton.Key].Render(
        role = role,
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        shape = shape,
        interactionSource = interactionSource,
        customization = customization,
        content = content
    )
}