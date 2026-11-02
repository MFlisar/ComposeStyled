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

val FluentuiSystemIconsSearch: ImageVector
    get() {
        if (_FluentuiSystemIconsSearch != null) return _FluentuiSystemIconsSearch!!

        _FluentuiSystemIconsSearch = ImageVector.Builder(
            name = "search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(15.8432f, 17.3679f)
                curveTo(14.4988f, 18.392f, 12.8204f, 19f, 11f, 19f)
                curveTo(6.58172f, 19f, 3f, 15.4183f, 3f, 11f)
                curveTo(3f, 6.58172f, 6.58172f, 3f, 11f, 3f)
                curveTo(15.4183f, 3f, 19f, 6.58172f, 19f, 11f)
                curveTo(19f, 12.8768f, 18.3537f, 14.6027f, 17.2715f, 15.9672f)
                lineTo(21.6982f, 20.2841f)
                curveTo(22.0936f, 20.6697f, 22.1015f, 21.3028f, 21.7159f, 21.6982f)
                curveTo(21.3303f, 22.0936f, 20.6972f, 22.1015f, 20.3018f, 21.7159f)
                lineTo(15.8432f, 17.3679f)
                close()
                moveTo(17f, 11f)
                curveTo(17f, 7.68629f, 14.3137f, 5f, 11f, 5f)
                curveTo(7.68629f, 5f, 5f, 7.68629f, 5f, 11f)
                curveTo(5f, 14.3137f, 7.68629f, 17f, 11f, 17f)
                curveTo(14.3137f, 17f, 17f, 14.3137f, 17f, 11f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsSearch!!
    }

private var _FluentuiSystemIconsSearch: ImageVector? = null