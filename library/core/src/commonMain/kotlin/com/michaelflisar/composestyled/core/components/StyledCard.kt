package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.classes.colors.StatefulBaseColorDef
import com.michaelflisar.composestyled.core.renderer.LocalStyledComponents
import com.michaelflisar.composestyled.core.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.core.runtime.LocalThemeBuilder
import com.michaelflisar.composestyled.core.runtime.interaction.rememberStyledResolveState

object StyledCard : BaseStyledComponent {

    internal val Property = ThemeProperty<StatefulBaseColorDef>("card")

    internal val TokenFilled = ThemeToken<StatefulBaseColorDef>("card.filled.default")
    internal val TokenOutlined = ThemeToken<StatefulBaseColorDef>("card.outlined.default")

    sealed class Variant {
        companion object {
            val Filled: Variant = Token(TokenFilled)
            val Outlined: Variant = Token(TokenOutlined)

            fun custom(colorDef: StatefulBaseColorDef): Variant = Custom(colorDef)
        }

        internal data class Token(val token: ThemeToken<StatefulBaseColorDef>) : Variant()
        internal data class Custom(val colorDef: StatefulBaseColorDef) : Variant()
    }

    @InternalComposeStyledApi
    @Composable
    fun registerVariantStyles(
        filled: StatefulBaseColorDef,
        outlined: StatefulBaseColorDef,
    ) {
        with(LocalThemeBuilder.current) {
            properties[Property] = mapOf(
                TokenFilled to filled,
                TokenOutlined to outlined,
            )
        }
    }
}

/** Defaults for [StyledCard]. */
object StyledCardDefaults {

    @Composable
    fun contentPadding(): PaddingValues = PaddingValues(0.dp)

    val borderWidth: Dp = Dp.Hairline
}

/**
 * Styled card container.
 *
 * Note: `outlined` exists for backwards-compat with the demo usage.
 */
@Composable
fun StyledCard(
    modifier: Modifier = Modifier,
    shape: Shape = StyledTheme.shapes.card,
    outlined: Boolean = false,
    enabled: Boolean = true,
    contentPadding: PaddingValues = StyledCardDefaults.contentPadding(),
    content: @Composable ColumnScope.() -> Unit,
) {
    val variant = if (outlined) StyledCard.Variant.Outlined else StyledCard.Variant.Filled

    val colorDef = when (variant) {
        is StyledCard.Variant.Token -> Theme[StyledCard.Property][variant.token]
        is StyledCard.Variant.Custom -> variant.colorDef
    }

    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

    val resolveState = rememberStyledResolveState(
        interactionSource = interactionSource,
        enabled = enabled,
        isError = false,
    )

    val resolved = colorDef.resolve(resolveState)

    val border = resolved.border?.let { BorderStroke(width = StyledCardDefaults.borderWidth, color = it) }

    LocalStyledComponents.current.Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = resolved.background,
        contentColor = resolved.foreground,
        border = border,
        contentPadding = contentPadding,
        content = content,
    )
}
