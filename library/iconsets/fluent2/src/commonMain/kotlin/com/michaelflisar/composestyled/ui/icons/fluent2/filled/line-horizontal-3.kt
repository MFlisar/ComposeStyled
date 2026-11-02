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

val FluentuiSystemIconsLineHorizontal3: ImageVector
    get() {
        if (_FluentuiSystemIconsLineHorizontal3 != null) return _FluentuiSystemIconsLineHorizontal3!!

        _FluentuiSystemIconsLineHorizontal3 = ImageVector.Builder(
            name = "line-horizontal-3",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(3f, 17f)
                horizontalLineTo(21f)
                curveTo(21.5523f, 17f, 22f, 17.4477f, 22f, 18f)
                curveTo(22f, 18.5128f, 21.614f, 18.9355f, 21.1166f, 18.9933f)
                lineTo(21f, 19f)
                horizontalLineTo(3f)
                curveTo(2.44772f, 19f, 2f, 18.5523f, 2f, 18f)
                curveTo(2f, 17.4872f, 2.38604f, 17.0645f, 2.88338f, 17.0067f)
                lineTo(3f, 17f)
                horizontalLineTo(21f)
                horizontalLineTo(3f)
                close()
                moveTo(2.99988f, 11f)
                lineTo(20.9999f, 10.9978f)
                curveTo(21.5522f, 10.9978f, 22f, 11.4454f, 22f, 11.9977f)
                curveTo(22f, 12.5106f, 21.6141f, 12.9333f, 21.1167f, 12.9911f)
                lineTo(21.0001f, 12.9978f)
                lineTo(3.00012f, 13f)
                curveTo(2.44784f, 13.0001f, 2f, 12.5524f, 2f, 12.0001f)
                curveTo(2f, 11.4873f, 2.38594f, 11.0646f, 2.88326f, 11.0067f)
                lineTo(2.99988f, 11f)
                lineTo(20.9999f, 10.9978f)
                lineTo(2.99988f, 11f)
                close()
                moveTo(3f, 5f)
                horizontalLineTo(21f)
                curveTo(21.5523f, 5f, 22f, 5.44772f, 22f, 6f)
                curveTo(22f, 6.51284f, 21.614f, 6.93551f, 21.1166f, 6.99327f)
                lineTo(21f, 7f)
                horizontalLineTo(3f)
                curveTo(2.44772f, 7f, 2f, 6.55228f, 2f, 6f)
                curveTo(2f, 5.48716f, 2.38604f, 5.06449f, 2.88338f, 5.00673f)
                lineTo(3f, 5f)
                horizontalLineTo(21f)
                horizontalLineTo(3f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsLineHorizontal3!!
    }

private var _FluentuiSystemIconsLineHorizontal3: ImageVector? = null