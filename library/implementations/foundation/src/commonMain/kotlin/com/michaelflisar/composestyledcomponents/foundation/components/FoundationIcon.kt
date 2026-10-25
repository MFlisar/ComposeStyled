package com.michaelflisar.composestyledcomponents.foundation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.classes.IconComposable
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

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
        com.michaelflisar.composestyledcomponents.classes.IconComposable(
            icon = icon,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
    }
}
