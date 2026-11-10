package com.michaelflisar.composestyled.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.tokens.LocalStyledContentColor

/** Loads an Android drawable resource and renders it using the active theme's icon styling. */
@Composable
fun StyledIcon(
    @DrawableRes id: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp? = StyledIconDefaults.size,
    tint: Color = LocalStyledContentColor.current,
) = StyledIcon(
    icon = id.toIconComposable(),
    modifier = modifier,
    contentDescription = contentDescription,
    size = size,
    tint = tint,
)
