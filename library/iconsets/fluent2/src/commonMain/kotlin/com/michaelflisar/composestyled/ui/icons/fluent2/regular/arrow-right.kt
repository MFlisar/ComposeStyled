/*
MIT License

Copyright (c) 2020 Microsoft Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.michaelflisar.composestyled.ui.icons.fluent2.regular

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val FluentuiSystemIconsArrowRight: ImageVector
    get() {
        if (_FluentuiSystemIconsArrowRight != null) return _FluentuiSystemIconsArrowRight!!
        
        _FluentuiSystemIconsArrowRight = ImageVector.Builder(
            name = "arrow-right",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(13.2673f, 4.20889f)
                curveTo(12.9674f, 3.9232f, 12.4926f, 3.93475f, 12.2069f, 4.23467f)
                curveTo(11.9212f, 4.5346f, 11.9328f, 5.00933f, 12.2327f, 5.29502f)
                lineTo(18.4841f, 11.2496f)
                horizontalLineTo(3.75f)
                curveTo(3.33579f, 11.2496f, 3f, 11.5854f, 3f, 11.9996f)
                curveTo(3f, 12.4138f, 3.33579f, 12.7496f, 3.75f, 12.7496f)
                horizontalLineTo(18.4842f)
                lineTo(12.2327f, 18.7043f)
                curveTo(11.9328f, 18.99f, 11.9212f, 19.4648f, 12.2069f, 19.7647f)
                curveTo(12.4926f, 20.0646f, 12.9674f, 20.0762f, 13.2673f, 19.7905f)
                lineTo(20.6862f, 12.7238f)
                curveTo(20.8551f, 12.5629f, 20.9551f, 12.3576f, 20.9861f, 12.1443f)
                curveTo(20.9952f, 12.0975f, 21f, 12.0491f, 21f, 11.9996f)
                curveTo(21f, 11.9501f, 20.9952f, 11.9016f, 20.986f, 11.8547f)
                curveTo(20.955f, 11.6415f, 20.855f, 11.4364f, 20.6862f, 11.2756f)
                lineTo(13.2673f, 4.20889f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsArrowRight!!
    }

private var _FluentuiSystemIconsArrowRight: ImageVector? = null