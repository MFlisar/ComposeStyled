package com.michaelflisar.demo.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText

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
