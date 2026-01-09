package com.michaelflisar.composestyled.theme.material3.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.BaseColorDef
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.components.StyledCard
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.tokens.StyledColors

object StyledCardImpl {

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    fun registerVariantStyles() {

        val colors = StyledTheme.colors

        val filled = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = colors.surface,
                foreground = colors.onSurface,
                border = null,
            )
        )

        val outlined = StatefulBaseColorDef(
            normal = BaseColorDef(
                background = Color.Transparent,
                foreground = colors.onSurface,
                border = colors.outlineVariant,
            )
        )

        StyledCard.registerVariantStyles(
            filled = filled,
            outlined = outlined,
        )
    }

    /** Android/Unstyled-based card implementation. */
    @Composable
    fun Render(
        modifier: Modifier,
        shape: Shape,
        backgroundColor: Color,
        contentColor: Color,
        border: BorderStroke?,
        contentPadding: PaddingValues,
        content: @Composable ColumnScope.() -> Unit,
    ) {
        ProvideStyledLocals(
            contentColor = contentColor,
            backgroundColor = backgroundColor,
        ) {
            Column(
                modifier = modifier
                    .clip(shape)
                    .background(backgroundColor)
                    .then(if (border != null) Modifier.border(border, shape) else Modifier)
                    .padding(contentPadding),
                content = content,
            )
        }
    }
}
