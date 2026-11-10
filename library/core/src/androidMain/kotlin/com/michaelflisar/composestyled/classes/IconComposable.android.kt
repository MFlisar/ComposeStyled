package com.michaelflisar.composestyled.classes

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource

/** Loads and adapts an Android drawable resource to the icon slot. */
fun Int.toIconComposable(
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
