package com.michaelflisar.composestyled.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.runtime.LocalContentColor

@Composable
fun StyledButtonContent(
    text: String,
    icon: ImageVector?,
    iconRotation: Float = 0f,
    iconTint: Color? = null,
) {
    if (icon == null) {
        StyledText(text)
    } else {
        val density = LocalDensity.current

        val lineHeight = StyledTextDefaults.style.lineHeight
        val fallbackIconSizeSp = StyledTextDefaults.style.fontSize
        val iconSizeSp = if (lineHeight.type == TextUnitType.Sp) lineHeight else fallbackIconSizeSp
        val iconSizeDp = with(density) { iconSizeSp.toDp() }

        if (text.isEmpty()) {
            StyledIcon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(iconSizeDp).rotate(iconRotation),
                tint = iconTint ?: LocalContentColor.current
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(StyledTheme.spacings.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StyledIcon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSizeDp).rotate(iconRotation),
                    tint = iconTint ?: LocalContentColor.current
                )
                StyledText(text)
            }
        }
    }
}