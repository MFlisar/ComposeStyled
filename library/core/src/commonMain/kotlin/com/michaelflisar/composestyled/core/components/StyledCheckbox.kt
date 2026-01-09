package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.icons.Check
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalThemeBuilder
import com.michaelflisar.composestyled.core.runtime.LocalContentColor
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledCheckbox : BaseStyledComponent {

    internal val Property = ThemeProperty<StatefulBaseColorDef>("checkbox")

    internal val TokenUnchecked = ThemeToken<StatefulBaseColorDef>("checkbox.unchecked")
    internal val TokenChecked = ThemeToken<StatefulBaseColorDef>("checkbox.checked")

    sealed class Variant {
        companion object {
            val Default: Variant = Token(unchecked = TokenUnchecked, checked = TokenChecked)

            fun custom(
                unchecked: StatefulBaseColorDef,
                checked: StatefulBaseColorDef,
            ): Variant = Custom(unchecked = unchecked, checked = checked)
        }

        internal data class Token(
            val unchecked: ThemeToken<StatefulBaseColorDef>,
            val checked: ThemeToken<StatefulBaseColorDef>,
        ) : Variant()

        internal data class Custom(
            val unchecked: StatefulBaseColorDef,
            val checked: StatefulBaseColorDef,
        ) : Variant()
    }

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        unchecked: StatefulBaseColorDef,
        checked: StatefulBaseColorDef,
    ) {
        with(LocalThemeBuilder.current) {
            properties[Property] = mapOf(
                TokenUnchecked to unchecked,
                TokenChecked to checked,
            )
        }
    }
}

object StyledCheckboxDefaults {
    @Composable
    fun defaultCheckIcon(color: Color = LocalContentColor.current) {
        StyledIcon(
            imageVector = Check,
            contentDescription = null,
            tint = color
        )
    }
}

@Composable
fun StyledCheckbox(
    checked: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = Color.Unspecified,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    shape: Shape = StyledTheme.shapes.checkbox,
    borderColor: Color = Color.Unspecified,
    borderWidth: Dp = 1.dp,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    contentDescription: String? = null,
    checkIcon: @Composable () -> Unit = { StyledCheckboxDefaults.defaultCheckIcon() },
    isError: Boolean = false,
    variant: StyledCheckbox.Variant = StyledCheckbox.Variant.Default,
) {
    val actualInteractionSource = interactionSource ?: remember { MutableInteractionSource() }

    val colorDef = when (variant) {
        is StyledCheckbox.Variant.Token -> {
            val map = Theme[StyledCheckbox.Property]
            if (checked) map[variant.checked] else map[variant.unchecked]
        }

        is StyledCheckbox.Variant.Custom -> if (checked) variant.checked else variant.unchecked
    }

    val resolveState = rememberStyledResolveState(
        interactionSource = actualInteractionSource,
        enabled = enabled,
        isError = isError,
    )
    val resolved = colorDef.resolve(resolveState)

    val bg = if (backgroundColor != Color.Unspecified) backgroundColor else resolved.background
    val fg = if (contentColor != Color.Unspecified) contentColor else resolved.foreground
    val br = (if (borderColor != Color.Unspecified) borderColor else resolved.border) ?: Color.Unspecified

    LocalStyledComponents.current.Checkbox(
        checked = checked,
        modifier = modifier,
        backgroundColor = bg,
        contentColor = fg,
        enabled = enabled,
        onCheckedChange = onCheckedChange,
        shape = shape,
        borderColor = br,
        borderWidth = borderWidth,
        interactionSource = actualInteractionSource,
        indication = indication,
        contentDescription = contentDescription,
        checkIcon = checkIcon,
    )
}