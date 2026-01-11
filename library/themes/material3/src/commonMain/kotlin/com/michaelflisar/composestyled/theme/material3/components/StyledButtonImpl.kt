package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.composeunstyled.UnstyledButton
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledButtonTokenRenderer
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.theme.material3.Material3Util

internal object StyledButtonImpl : StyledButtonTokenRenderer {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun registerVariantStyles() {

        val colors = StyledTheme.colors

        val buttonFilledPrimary = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = colors.primary,
                foreground = colors.onPrimary,
                border = null
            ),
            hovered = BaseColorDef(
                background = Material3Util.hoverFromBackground(colors.primary),
                foreground = colors.onPrimary,
                border = null
            )
        )
        val buttonOutlined = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onBackground,
                border = colors.outlineVariant
            ),
            hovered = BaseColorDef(
                background = Material3Util.hoverFromForeground(colors.onBackground),
                foreground = colors.onBackground,
                border = colors.outlineVariant
            ),
        )
        val buttonText = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onBackground,
                border = null
            ),
            hovered = BaseColorDef(
                background = Material3Util.hoverFromForeground(colors.onBackground),
                foreground = colors.onBackground,
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
    override fun Render(
        onClick: () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        borderColor: Color?,
        contentPadding: PaddingValues,
        interactionSource: MutableInteractionSource,
        content: @Composable (RowScope.() -> Unit),
    ) {
        ProvideStyledLocals(
            contentColor = contentColor,
            backgroundColor = backgroundColor,
        ) {
            UnstyledButton(
                onClick = onClick,
                enabled = enabled,
                shape = shape,
                backgroundColor = backgroundColor,
                contentColor = contentColor,
                contentPadding = contentPadding,
                borderColor = borderColor ?: Color.Unspecified,
                borderWidth = if (borderColor != null) 1.dp else 0.dp,
                modifier = modifier.hoverable(
                    interactionSource = interactionSource,
                    enabled = enabled
                ),
                role = Role.Button,
                indication = LocalIndication.current,
                interactionSource = interactionSource,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }

}
