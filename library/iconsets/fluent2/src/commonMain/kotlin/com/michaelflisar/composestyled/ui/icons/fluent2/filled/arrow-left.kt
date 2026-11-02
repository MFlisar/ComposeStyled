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

val FluentuiSystemIconsArrowLeft: ImageVector
    get() {
        if (_FluentuiSystemIconsArrowLeft != null) return _FluentuiSystemIconsArrowLeft!!

        _FluentuiSystemIconsArrowLeft = ImageVector.Builder(
            name = "arrow-left",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(10.2949f, 19.7152f)
                curveTo(10.6883f, 20.1028f, 11.3215f, 20.0981f, 11.7091f, 19.7047f)
                curveTo(12.0967f, 19.3113f, 12.092f, 18.6782f, 11.6986f, 18.2906f)
                lineTo(6.32827f, 12.9992f)
                horizontalLineTo(19.9995f)
                curveTo(20.5517f, 12.9992f, 20.9995f, 12.5514f, 20.9995f, 11.9992f)
                curveTo(20.9995f, 11.4469f, 20.5517f, 10.9992f, 19.9995f, 10.9992f)
                horizontalLineTo(6.33488f)
                lineTo(11.6986f, 5.71428f)
                curveTo(12.092f, 5.32666f, 12.0967f, 4.69351f, 11.7091f, 4.3001f)
                curveTo(11.3215f, 3.9067f, 10.6883f, 3.90201f, 10.2949f, 4.28963f)
                lineTo(3.37073f, 11.112f)
                curveTo(2.87382f, 11.6016f, 2.87382f, 12.4032f, 3.37073f, 12.8928f)
                lineTo(10.2949f, 19.7152f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsArrowLeft!!
    }

private var _FluentuiSystemIconsArrowLeft: ImageVector? = null