package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.ColorRef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.icons.Check
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.LocalStyledContentColor
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledCheckbox {

    // variants
    enum class Variant {
        Default
    }

    fun customize(
        unchecked: ColorRef? = null,
        checked: ColorRef? = null,
    ) = Customization(
        unchecked = unchecked,
        checked = checked,
    )

    @Immutable
    class Customization internal constructor(
        val unchecked: ColorRef?,
        val checked: ColorRef?,
    )
}

// ----------------------
// Renderer
// ----------------------

sealed interface StyledCheckboxRenderer {

    interface TokenRenderer : StyledCheckboxRenderer {

        @Composable
        fun resolveColors(
            variant: StyledCheckbox.Variant,
            checked: Boolean,
            customization: StyledCheckbox.Customization?,
        ): StatefulBaseColorDef

        @Composable
        fun render(
            checked: Boolean,
            modifier: Modifier,
            backgroundColor: Color,
            contentColor: Color,
            enabled: Boolean,
            onCheckedChange: ((Boolean) -> Unit)?,
            shape: Shape,
            borderColor: Color?,
            borderWidth: Dp,
            interactionSource: MutableInteractionSource,
            indication: Indication?,
            contentDescription: String?,
            checkIcon: @Composable () -> Unit,
        )
    }

    interface WrapperRenderer : StyledCheckboxRenderer {

        @Composable
        fun render(
            variant: StyledCheckbox.Variant,
            customization: StyledCheckbox.Customization?,
            checked: Boolean,
            onCheckedChange: ((Boolean) -> Unit)?,
            modifier: Modifier,
            enabled: Boolean,
            shape: Shape,
            borderWidth: Dp,
            interactionSource: MutableInteractionSource,
            indication: Indication?,
            contentDescription: String?,
            checkIcon: @Composable () -> Unit,
        )
    }

}

// ----------------------
// Defaults
// ----------------------

object StyledCheckboxDefaults {

    val variant = StyledCheckbox.Variant.Default

    val BorderWidth: Dp = 1.dp

    val shape: Shape
        @Composable @ReadOnlyComposable get() = StyledTheme.shapes.control

    @Composable
    fun CheckIcon(
        color: Color = LocalStyledContentColor.current,
    ) {
        StyledIcon(
            imageVector = Check,
            contentDescription = null,
            tint = color,
        )
    }
}

// ----------------------
// Composable
// ----------------------

@Composable
fun StyledCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    variant: StyledCheckbox.Variant = StyledCheckboxDefaults.variant,
    customization: StyledCheckbox.Customization? = null,
    enabled: Boolean = true,
    shape: Shape = StyledCheckboxDefaults.shape,
    borderWidth: Dp = StyledCheckboxDefaults.BorderWidth,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    contentDescription: String? = null,
    checkIcon: @Composable () -> Unit = { StyledCheckboxDefaults.CheckIcon() },
    isError: Boolean = false,
) {
    when (val renderer = LocalStyledComponents.current.checkbox) {
        is StyledCheckboxRenderer.TokenRenderer -> {
            val state = rememberStyledResolveState(
                interactionSource = interactionSource,
                enabled = enabled,
                isError = isError,
            )
            val colors = renderer
                .resolveColors(variant, checked, customization)
                .resolve(state)

            renderer.render(
                checked = checked,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                backgroundColor = colors.background,
                contentColor = colors.foreground,
                borderColor = colors.border,
                borderWidth = borderWidth,
                interactionSource = interactionSource,
                indication = indication,
                contentDescription = contentDescription,
                checkIcon = checkIcon,
                onCheckedChange = onCheckedChange,
            )
        }

        is StyledCheckboxRenderer.WrapperRenderer -> {
            renderer.render(
                variant = variant,
                customization = customization,
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                enabled = enabled,
                shape = shape,
                borderWidth = borderWidth,
                interactionSource = interactionSource,
                indication = indication,
                contentDescription = contentDescription,
                checkIcon = checkIcon,
            )
        }
    }
}
