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

val FluentuiSystemIconsSubtract: ImageVector
    get() {
        if (_FluentuiSystemIconsSubtract != null) return _FluentuiSystemIconsSubtract!!

        _FluentuiSystemIconsSubtract = ImageVector.Builder(
            name = "subtract",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(3.75391f, 12.5f)
                horizontalLineTo(20.2458f)
                curveTo(20.66f, 12.5f, 20.9958f, 12.1642f, 20.9958f, 11.75f)
                curveTo(20.9958f, 11.3358f, 20.66f, 11f, 20.2458f, 11f)
                horizontalLineTo(3.75391f)
                curveTo(3.33969f, 11f, 3.00391f, 11.3358f, 3.00391f, 11.75f)
                curveTo(3.00391f, 12.1642f, 3.33969f, 12.5f, 3.75391f, 12.5f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsSubtract!!
    }

private var _FluentuiSystemIconsSubtract: ImageVector? = null