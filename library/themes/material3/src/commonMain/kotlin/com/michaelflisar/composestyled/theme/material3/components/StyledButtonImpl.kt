package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.michaelflisar.composestyled.core.classes.colors.BaseColor
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.tokens.StyledColors
import com.michaelflisar.composestyled.theme.material3.Material3

internal object StyledButtonImpl {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    fun registerVariantStyles(
        colors: StyledColors,
    ) {
        val buttonFilledPrimary = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = colors.primary,
                foreground = colors.onPrimary,
                border = null
            ),
            hovered = BaseColorDef(
                background = colors.primary.copy(alpha = Material3.AlphaHover),
                foreground = colors.onPrimary,
                border = null
            )
        )
        val buttonOutlined = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.primary,
                border = colors.outlineVariant
            ),
            hovered = BaseColorDef(
                background = colors.primary.copy(alpha = Material3.AlphaHover),
                foreground = colors.primary,
                border = colors.outlineVariant
            ),
        )
        val buttonText = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.primary,
                border = null
            ),
            hovered = BaseColorDef(
                background = colors.primary.copy(alpha = Material3.AlphaHover),
                foreground = colors.primary,
                border = null
            )
        )

        StyledButton.registerVariantStyles(
            primary = buttonFilledPrimary,
            outlined = buttonOutlined,
            text = buttonText
        )
    }


    /**
     * Android/Unstyled-based button.
     */
    @Composable
    fun Render(
        colors: BaseColor,
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable RowScope.() -> Unit,
    ) {
        UnstyledButton(
            onClick = onClick,
            modifier = modifier.hoverable(
                interactionSource = interactionSource,
                enabled = enabled
            ),
            enabled = enabled,
            shape = shape,
            contentPadding = contentPadding,
            indication = LocalIndication.current,
            interactionSource = interactionSource,
            backgroundColor = colors.background,
            contentColor = colors.foreground,
            borderColor = colors.border ?: Color.Unspecified,
            borderWidth = if (colors.border != null) 1.dp else 0.dp,
            content = content
        )
    }

}
