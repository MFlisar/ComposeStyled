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

val FluentuiSystemIconsDismiss: ImageVector
    get() {
        if (_FluentuiSystemIconsDismiss != null) return _FluentuiSystemIconsDismiss!!

        _FluentuiSystemIconsDismiss = ImageVector.Builder(
            name = "dismiss",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(4.2097f, 4.3871f)
                lineTo(4.29289f, 4.29289f)
                curveTo(4.65338f, 3.93241f, 5.22061f, 3.90468f, 5.6129f, 4.2097f)
                lineTo(5.70711f, 4.29289f)
                lineTo(12f, 10.585f)
                lineTo(18.2929f, 4.29289f)
                curveTo(18.6834f, 3.90237f, 19.3166f, 3.90237f, 19.7071f, 4.29289f)
                curveTo(20.0976f, 4.68342f, 20.0976f, 5.31658f, 19.7071f, 5.70711f)
                lineTo(13.415f, 12f)
                lineTo(19.7071f, 18.2929f)
                curveTo(20.0676f, 18.6534f, 20.0953f, 19.2206f, 19.7903f, 19.6129f)
                lineTo(19.7071f, 19.7071f)
                curveTo(19.3466f, 20.0676f, 18.7794f, 20.0953f, 18.3871f, 19.7903f)
                lineTo(18.2929f, 19.7071f)
                lineTo(12f, 13.415f)
                lineTo(5.70711f, 19.7071f)
                curveTo(5.31658f, 20.0976f, 4.68342f, 20.0976f, 4.29289f, 19.7071f)
                curveTo(3.90237f, 19.3166f, 3.90237f, 18.6834f, 4.29289f, 18.2929f)
                lineTo(10.585f, 12f)
                lineTo(4.29289f, 5.70711f)
                curveTo(3.93241f, 5.34662f, 3.90468f, 4.77939f, 4.2097f, 4.3871f)
                lineTo(4.29289f, 4.29289f)
                lineTo(4.2097f, 4.3871f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsDismiss!!
    }

private var _FluentuiSystemIconsDismiss: ImageVector? = null