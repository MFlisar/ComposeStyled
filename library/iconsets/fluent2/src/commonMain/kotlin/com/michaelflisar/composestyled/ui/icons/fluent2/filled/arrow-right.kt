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
package com.michaelflisar.composestyled.ui.icons.fluent2.filled

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
                moveTo(13.7045f, 4.28377f)
                curveTo(13.3111f, 3.89615f, 12.678f, 3.90084f, 12.2904f, 4.29424f)
                curveTo(11.9027f, 4.68765f, 11.9074f, 5.3208f, 12.3008f, 5.70842f)
                lineTo(17.6712f, 10.9998f)
                horizontalLineTo(4f)
                curveTo(3.44771f, 10.9998f, 3f, 11.4475f, 3f, 11.9998f)
                curveTo(3f, 12.5521f, 3.44772f, 12.9998f, 4f, 12.9998f)
                horizontalLineTo(17.6646f)
                lineTo(12.3008f, 18.2847f)
                curveTo(11.9074f, 18.6723f, 11.9027f, 19.3055f, 12.2904f, 19.6989f)
                curveTo(12.678f, 20.0923f, 13.3111f, 20.097f, 13.7045f, 19.7094f)
                lineTo(20.6287f, 12.887f)
                curveTo(21.1256f, 12.3974f, 21.1256f, 11.5958f, 20.6287f, 11.1062f)
                lineTo(13.7045f, 4.28377f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsArrowRight!!
    }

private var _FluentuiSystemIconsArrowRight: ImageVector? = null