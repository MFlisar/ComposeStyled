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

val FluentuiSystemIconsChevronDown: ImageVector
    get() {
        if (_FluentuiSystemIconsChevronDown != null) return _FluentuiSystemIconsChevronDown!!

        _FluentuiSystemIconsChevronDown = ImageVector.Builder(
            name = "chevron-down",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(4.29289f, 8.29289f)
                curveTo(4.68342f, 7.90237f, 5.31658f, 7.90237f, 5.70711f, 8.29289f)
                lineTo(12f, 14.5858f)
                lineTo(18.2929f, 8.29289f)
                curveTo(18.6834f, 7.90237f, 19.3166f, 7.90237f, 19.7071f, 8.29289f)
                curveTo(20.0976f, 8.68342f, 20.0976f, 9.31658f, 19.7071f, 9.70711f)
                lineTo(12.7071f, 16.7071f)
                curveTo(12.3166f, 17.0976f, 11.6834f, 17.0976f, 11.2929f, 16.7071f)
                lineTo(4.29289f, 9.70711f)
                curveTo(3.90237f, 9.31658f, 3.90237f, 8.68342f, 4.29289f, 8.29289f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsChevronDown!!
    }

private var _FluentuiSystemIconsChevronDown: ImageVector? = null