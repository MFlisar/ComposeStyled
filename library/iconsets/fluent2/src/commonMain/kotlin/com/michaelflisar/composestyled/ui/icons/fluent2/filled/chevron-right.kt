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

val FluentuiSystemIconsChevronRight: ImageVector
    get() {
        if (_FluentuiSystemIconsChevronRight != null) return _FluentuiSystemIconsChevronRight!!
        
        _FluentuiSystemIconsChevronRight = ImageVector.Builder(
            name = "chevron-right",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(8.29289f, 4.29289f)
                curveTo(7.90237f, 4.68342f, 7.90237f, 5.31658f, 8.29289f, 5.70711f)
                lineTo(14.5858f, 12f)
                lineTo(8.29289f, 18.2929f)
                curveTo(7.90237f, 18.6834f, 7.90237f, 19.3166f, 8.29289f, 19.7071f)
                curveTo(8.68342f, 20.0976f, 9.31658f, 20.0976f, 9.70711f, 19.7071f)
                lineTo(16.7071f, 12.7071f)
                curveTo(17.0976f, 12.3166f, 17.0976f, 11.6834f, 16.7071f, 11.2929f)
                lineTo(9.70711f, 4.29289f)
                curveTo(9.31658f, 3.90237f, 8.68342f, 3.90237f, 8.29289f, 4.29289f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsChevronRight!!
    }

private var _FluentuiSystemIconsChevronRight: ImageVector? = null