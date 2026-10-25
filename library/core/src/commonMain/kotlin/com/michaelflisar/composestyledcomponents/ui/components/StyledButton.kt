package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyledcomponents.classes.ColorToken
import com.michaelflisar.composestyledcomponents.classes.CustomColors
import com.michaelflisar.composestyledcomponents.classes.asColorToken
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey
import com.michaelflisar.composestyledcomponents.ui.components.StyledButton.Customization

object StyledButton {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledButton")

    // -------------
    // 2) defaults
    // -------------

    @Stable
    class Defaults(
        val shape: Shape,
    )

    // -------------
    // 3) roles / variants
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
            content: @Composable RowScope.() -> Unit,
        )
    }
}

// -------------------------
// defaults
// -------------------------

object StyledButtonDefaults {

    val shape: Shape
        @Composable get() = LocalStyledRegistry.current[StyledButton.Key].defaults.shape

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

@Composable
fun StyledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) {
    StyledButtonImpl(
        role = StyledButton.Role.Primary,
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
fun StyledPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) = StyledButtonImpl(
    role = StyledButton.Role.Primary,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    shape = shape,
    interactionSource = interactionSource,
    customization = customization,
    content = content
)

@Composable
fun StyledSecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) = StyledButtonImpl(
    role = StyledButton.Role.Secondary,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    shape = shape,
    interactionSource = interactionSource,
    customization = customization,
    content = content
)

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

@Composable
fun StyledTertiaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    // defaults / customisation
    shape: Shape = StyledButtonDefaults.shape,
    customization: Customization? = null,
    // content
    content: @Composable RowScope.() -> Unit,
) = StyledButtonImpl(
    role = StyledButton.Role.Tertiary,
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    shape = shape,
    interactionSource = interactionSource,
    customization = customization,
    content = content
)