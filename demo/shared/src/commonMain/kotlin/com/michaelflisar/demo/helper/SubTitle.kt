package com.michaelflisar.demo.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.ui.primitives.StyledText

@Composable
internal fun SubTitle(
    title: String,
) {
    StyledText(
        text = title,
        fontWeight = FontWeight.Bold,
        style = StyledTheme.typography.titleSmall
    )
}
