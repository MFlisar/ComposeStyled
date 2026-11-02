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

val FluentuiSystemIconsCheckmark: ImageVector
    get() {
        if (_FluentuiSystemIconsCheckmark != null) return _FluentuiSystemIconsCheckmark!!

        _FluentuiSystemIconsCheckmark = ImageVector.Builder(
            name = "checkmark",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(8.5f, 16.5858f)
                lineTo(4.70711f, 12.7929f)
                curveTo(4.31658f, 12.4024f, 3.68342f, 12.4024f, 3.29289f, 12.7929f)
                curveTo(2.90237f, 13.1834f, 2.90237f, 13.8166f, 3.29289f, 14.2071f)
                lineTo(7.79289f, 18.7071f)
                curveTo(8.18342f, 19.0976f, 8.81658f, 19.0976f, 9.20711f, 18.7071f)
                lineTo(20.2071f, 7.70711f)
                curveTo(20.5976f, 7.31658f, 20.5976f, 6.68342f, 20.2071f, 6.29289f)
                curveTo(19.8166f, 5.90237f, 19.1834f, 5.90237f, 18.7929f, 6.29289f)
                lineTo(8.5f, 16.5858f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsCheckmark!!
    }

private var _FluentuiSystemIconsCheckmark: ImageVector? = null