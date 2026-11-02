package com.michaelflisar.composestyled.foundation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

/** Shared Image-based icon renderer for non-Material themes. */
@InternalComposeStyledApi
class FoundationIcon : StyledIcon.Implementation {

    @Composable
    override fun Render(
        icon: IconComposable,
        modifier: Modifier,
        contentDescription: String?,
        tint: Color,
    ) {
        com.michaelflisar.composestyled.classes.IconComposable(
            icon = icon,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
    }
}
