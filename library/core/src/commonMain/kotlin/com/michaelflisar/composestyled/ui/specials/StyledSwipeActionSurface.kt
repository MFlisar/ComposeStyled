package com.michaelflisar.composestyled.ui.specials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyled.UpdateStyledThemeLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi

@Composable
@OptIn(InternalComposeStyledApi::class)
internal fun StyledSwipeActionSurface(
    modifier: Modifier = Modifier,
    shape: Shape,
    backgroundColor: Color,
    contentColor: Color,
    contentPadding: PaddingValues = PaddingValues(),
    contentAlignment: Alignment = Alignment.CenterStart,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor, shape),
        contentAlignment = contentAlignment,
    ) {
        UpdateStyledThemeLocals(contentColor = contentColor) {
            Box(modifier = Modifier.padding(contentPadding)) {
                content()
            }
        }
    }
}
