package com.michaelflisar.composestyled.components.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledButton
import com.michaelflisar.composestyled.core.components.StyledIcon
import com.michaelflisar.composestyled.core.components.StyledText

object StyledFloatingActionButtonDefaults {

    val SizeSmall = 40.dp
    val SizeRegular = 48.dp
    val SizeLarge = 56.dp

    val shape: Shape
        @Composable @ReadOnlyComposable get() = StyledTheme.shapes.rounded

    val iconSize: Dp
        @Composable @ReadOnlyComposable get() = StyledTheme.sizes.iconSize
}

@Composable
fun StyledFloatingActionButton(
    onClick: () -> Unit,
    imageVector: ImageVector,
    text: String? = null,
    fillWidth: Boolean = false,
    size: Dp = StyledFloatingActionButtonDefaults.SizeRegular,
    shape: Shape = StyledFloatingActionButtonDefaults.shape,
) {
    StyledButton(
        modifier = if (fillWidth) Modifier.height(size).fillMaxWidth() else Modifier.size(size),
        shape = shape,
        contentPadding = PaddingValues(),
        onClick = onClick
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (text?.isNotEmpty() == true) {
                StyledIcon(
                    modifier = Modifier.size(StyledFloatingActionButtonDefaults.iconSize),
                    imageVector = imageVector,
                    contentDescription = null
                )
                StyledText(text = text, style = StyledTheme.typography.labelMedium)
            } else {
                StyledIcon(
                    modifier = Modifier.size(StyledFloatingActionButtonDefaults.iconSize),
                    imageVector = imageVector,
                    contentDescription = null
                )
            }
        }
    }
}