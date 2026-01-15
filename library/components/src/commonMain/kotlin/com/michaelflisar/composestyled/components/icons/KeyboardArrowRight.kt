package com.michaelflisar.composestyled.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val KeyboardArrowRight: ImageVector
    get() {
        if (_KeyboardArrowRight != null) {
            return _KeyboardArrowRight!!
        }
        _KeyboardArrowRight = ImageVector.Builder(
            name = "KeyboardArrowRight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color(0xFFE3E3E3))) {
                moveTo(504f, 480f)
                lineTo(320f, 296f)
                lineToRelative(56f, -56f)
                lineToRelative(240f, 240f)
                lineToRelative(-240f, 240f)
                lineToRelative(-56f, -56f)
                lineToRelative(184f, -184f)
                close()
            }
        }.build()

        return _KeyboardArrowRight!!
    }

@Suppress("ObjectPropertyName")
private var _KeyboardArrowRight: ImageVector? = null