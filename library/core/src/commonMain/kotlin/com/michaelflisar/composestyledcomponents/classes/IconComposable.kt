package com.michaelflisar.composestyledcomponents.classes


import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.michaelflisar.composestyledcomponents.tokens.LocalStyledContentColor
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

typealias IconComposable = @Composable (contentDescription: String?, modifier: Modifier, tint: Color) -> Unit

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

@Composable
fun IconComposable(
    icon: IconComposable,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = LocalStyledContentColor.current,
) {
    icon.invoke(contentDescription, modifier, tint)
}