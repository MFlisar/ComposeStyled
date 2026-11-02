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

val FluentuiSystemIconsAdd: ImageVector
    get() {
        if (_FluentuiSystemIconsAdd != null) return _FluentuiSystemIconsAdd!!

        _FluentuiSystemIconsAdd = ImageVector.Builder(
            name = "add",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(11.8834f, 3.00673f)
                lineTo(12f, 3f)
                curveTo(12.5128f, 3f, 12.9355f, 3.38604f, 12.9933f, 3.88338f)
                lineTo(13f, 4f)
                verticalLineTo(11f)
                horizontalLineTo(20f)
                curveTo(20.5128f, 11f, 20.9355f, 11.386f, 20.9933f, 11.8834f)
                lineTo(21f, 12f)
                curveTo(21f, 12.5128f, 20.614f, 12.9355f, 20.1166f, 12.9933f)
                lineTo(20f, 13f)
                horizontalLineTo(13f)
                verticalLineTo(20f)
                curveTo(13f, 20.5128f, 12.614f, 20.9355f, 12.1166f, 20.9933f)
                lineTo(12f, 21f)
                curveTo(11.4872f, 21f, 11.0645f, 20.614f, 11.0067f, 20.1166f)
                lineTo(11f, 20f)
                verticalLineTo(13f)
                horizontalLineTo(4f)
                curveTo(3.48716f, 13f, 3.06449f, 12.614f, 3.00673f, 12.1166f)
                lineTo(3f, 12f)
                curveTo(3f, 11.4872f, 3.38604f, 11.0645f, 3.88338f, 11.0067f)
                lineTo(4f, 11f)
                horizontalLineTo(11f)
                verticalLineTo(4f)
                curveTo(11f, 3.48716f, 11.386f, 3.06449f, 11.8834f, 3.00673f)
                lineTo(12f, 3f)
                lineTo(11.8834f, 3.00673f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsAdd!!
    }

private var _FluentuiSystemIconsAdd: ImageVector? = null