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

val FluentuiSystemIconsFilter: ImageVector
    get() {
        if (_FluentuiSystemIconsFilter != null) return _FluentuiSystemIconsFilter!!
        
        _FluentuiSystemIconsFilter = ImageVector.Builder(
            name = "filter",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(10f, 16f)
                horizontalLineTo(14f)
                curveTo(14.5523f, 16f, 15f, 16.4477f, 15f, 17f)
                curveTo(15f, 17.5128f, 14.614f, 17.9355f, 14.1166f, 17.9933f)
                lineTo(14f, 18f)
                horizontalLineTo(10f)
                curveTo(9.44772f, 18f, 9f, 17.5523f, 9f, 17f)
                curveTo(9f, 16.4872f, 9.38604f, 16.0645f, 9.88338f, 16.0067f)
                lineTo(10f, 16f)
                horizontalLineTo(14f)
                horizontalLineTo(10f)
                close()
                moveTo(8f, 11f)
                horizontalLineTo(16f)
                curveTo(16.5523f, 11f, 17f, 11.4477f, 17f, 12f)
                curveTo(17f, 12.5128f, 16.614f, 12.9355f, 16.1166f, 12.9933f)
                lineTo(16f, 13f)
                horizontalLineTo(8f)
                curveTo(7.44772f, 13f, 7f, 12.5523f, 7f, 12f)
                curveTo(7f, 11.4872f, 7.38604f, 11.0645f, 7.88338f, 11.0067f)
                lineTo(8f, 11f)
                horizontalLineTo(16f)
                horizontalLineTo(8f)
                close()
                moveTo(5f, 6f)
                horizontalLineTo(19f)
                curveTo(19.5523f, 6f, 20f, 6.44772f, 20f, 7f)
                curveTo(20f, 7.51284f, 19.614f, 7.93551f, 19.1166f, 7.99327f)
                lineTo(19f, 8f)
                horizontalLineTo(5f)
                curveTo(4.44772f, 8f, 4f, 7.55228f, 4f, 7f)
                curveTo(4f, 6.48716f, 4.38604f, 6.06449f, 4.88338f, 6.00673f)
                lineTo(5f, 6f)
                horizontalLineTo(19f)
                horizontalLineTo(5f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsFilter!!
    }

private var _FluentuiSystemIconsFilter: ImageVector? = null