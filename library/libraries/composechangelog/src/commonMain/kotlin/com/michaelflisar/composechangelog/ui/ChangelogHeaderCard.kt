package com.michaelflisar.composechangelog.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.CustomColors
import com.michaelflisar.composestyled.ui.components.StyledCard
import com.michaelflisar.composestyled.ui.components.StyledCardDefaults
import com.michaelflisar.composestyled.ui.layout.StyledColumn
import com.michaelflisar.composestyled.ui.primitives.StyledText

/** A reusable card with a bold heading and custom Styled content. */
@Composable
fun ChangelogHeaderCard(
    title: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    colors: CustomColors? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val customization = colors?.let {
        StyledCardDefaults.customize(colors = it, elevation = 0.dp)
    }

    StyledCard(
        modifier = modifier,
        customization = customization,
    ) {
        StyledColumn(itemSpacing = StyledTheme.spacing.medium) {
            StyledText(
                text = title,
                style = StyledTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
            content()
        }
    }
}
