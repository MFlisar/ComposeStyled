package com.michaelflisar.composestyled.classes


import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * An icon slot that receives accessibility text, layout modifier and tint from its caller.
 *
 * Implementations should pass `contentDescription` to the rendered image for accessibility.
 */
typealias IconComposable = @Composable (contentDescription: String?, modifier: Modifier, tint: Color) -> Unit

/** Adapts an [ImageVector] to the icon slot, using the supplied tint unless tinting is disabled. */
fun ImageVector.toIconComposable(
    tint: Color = Color.Unspecified,
    disableTint: Boolean = false,
): IconComposable {
    return this.let {
        @Composable { contentDescription, modifier, tint2 ->
            Image(
                imageVector = this,
                contentDescription = contentDescription,
                modifier = modifier,
                colorFilter = (tint.takeIf { it != Color.Unspecified } ?: tint2)
                    .takeIf { !disableTint }
                    ?.let { ColorFilter.tint(it) },
            )
        }
    }
}

/** Adapts a [Painter] to the icon slot, using the supplied tint unless tinting is disabled. */
fun Painter.toIconComposable(
    tint: Color = Color.Unspecified,
    disableTint: Boolean = false,
): IconComposable {
    return this.let {
        @Composable { contentDescription, modifier, tint2 ->
            Image(
                painter = this,
                contentDescription = contentDescription,
                modifier = modifier,
                colorFilter = (tint.takeIf { it != Color.Unspecified } ?: tint2)
                    .takeIf { !disableTint }
                    ?.let { ColorFilter.tint(it) },
            )
        }
    }
}

/** Loads and adapts a Compose resource to the icon slot. */
fun DrawableResource.toIconComposable(
    tint: Color = Color.Unspecified,
    disableTint: Boolean = false,
): IconComposable {
    return this.let {
        @Composable { contentDescription, modifier, tint2 ->
            Image(
                painter = painterResource(this),
                contentDescription = contentDescription,
                modifier = modifier,
                colorFilter = (tint.takeIf { it != Color.Unspecified } ?: tint2)
                    .takeIf { !disableTint }
                    ?.let { ColorFilter.tint(it) },
            )
        }
    }
}

/** Renders [icon] with a content description, modifier and tint. */
@Composable
fun IconComposable(
    icon: IconComposable,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = LocalStyledContentColor.current,
) {
    icon.invoke(contentDescription, modifier, tint)
}