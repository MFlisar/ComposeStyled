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

val FluentuiSystemIconsChevronLeft: ImageVector
    get() {
        if (_FluentuiSystemIconsChevronLeft != null) return _FluentuiSystemIconsChevronLeft!!
        
        _FluentuiSystemIconsChevronLeft = ImageVector.Builder(
            name = "chevron-left",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(15.5303f, 4.21967f)
                curveTo(15.8232f, 4.51256f, 15.8232f, 4.98744f, 15.5303f, 5.28033f)
                lineTo(8.81066f, 12f)
                lineTo(15.5303f, 18.7197f)
                curveTo(15.8232f, 19.0126f, 15.8232f, 19.4874f, 15.5303f, 19.7803f)
                curveTo(15.2374f, 20.0732f, 14.7626f, 20.0732f, 14.4697f, 19.7803f)
                lineTo(7.21967f, 12.5303f)
                curveTo(6.92678f, 12.2374f, 6.92678f, 11.7626f, 7.21967f, 11.4697f)
                lineTo(14.4697f, 4.21967f)
                curveTo(14.7626f, 3.92678f, 15.2374f, 3.92678f, 15.5303f, 4.21967f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsChevronLeft!!
    }

private var _FluentuiSystemIconsChevronLeft: ImageVector? = null