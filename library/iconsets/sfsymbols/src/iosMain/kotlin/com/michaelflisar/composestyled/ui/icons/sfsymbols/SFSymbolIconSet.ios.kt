package com.michaelflisar.composestyled.ui.icons.sfsymbols

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.UIKitView
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import platform.UIKit.UIColor
import platform.UIKit.UIImage
import platform.UIKit.UIImageView

internal actual fun platformIcon(
    @Suppress("UNUSED_PARAMETER") icon: StyledIcons.Name,
    symbol: String,
): IconComposable =
    { contentDescription, modifier, tint ->
        sfSymbol(symbol, contentDescription, modifier, tint)
    }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun sfSymbol(
    name: String,
    description: String?,
    modifier: Modifier,
    tint: Color,
) {
    UIKitView(
        factory = {
            val image = UIImage.systemImageNamed(name)
                ?: error("SF Symbol '$name' is unavailable on this iOS version.")
            UIImageView().apply {
                this.image = image
                tintColor = tint.toUIColor()
            }
        },
        modifier = modifier.semantics {
            description?.let { this.contentDescription = it }
        },
        update = { imageView ->
            imageView.tintColor = tint.toUIColor()
        },
    )
}

private fun Color.toUIColor(): UIColor =
    if (this == Color.Unspecified) {
        UIColor.blackColor
    } else {
        UIColor(
            red = red.toDouble(),
            green = green.toDouble(),
            blue = blue.toDouble(),
            alpha = alpha.toDouble(),
        )
    }
