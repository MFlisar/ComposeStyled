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

val FluentuiSystemIconsHome: ImageVector
    get() {
        if (_FluentuiSystemIconsHome != null) return _FluentuiSystemIconsHome!!
        
        _FluentuiSystemIconsHome = ImageVector.Builder(
            name = "home",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(13.4508f, 2.53318f)
                curveTo(12.6128f, 1.82618f, 11.3872f, 1.82618f, 10.5492f, 2.53318f)
                lineTo(3.79916f, 8.22772f)
                curveTo(3.29241f, 8.65523f, 3f, 9.28447f, 3f, 9.94747f)
                verticalLineTo(19.2526f)
                curveTo(3f, 20.2191f, 3.7835f, 21.0026f, 4.75f, 21.0026f)
                horizontalLineTo(7.75f)
                curveTo(8.7165f, 21.0026f, 9.5f, 20.2191f, 9.5f, 19.2526f)
                verticalLineTo(15.25f)
                curveTo(9.5f, 14.5707f, 10.0418f, 14.018f, 10.7169f, 14.0004f)
                horizontalLineTo(13.2831f)
                curveTo(13.9582f, 14.018f, 14.5f, 14.5707f, 14.5f, 15.25f)
                verticalLineTo(19.2526f)
                curveTo(14.5f, 20.2191f, 15.2835f, 21.0026f, 16.25f, 21.0026f)
                horizontalLineTo(19.25f)
                curveTo(20.2165f, 21.0026f, 21f, 20.2191f, 21f, 19.2526f)
                verticalLineTo(9.94747f)
                curveTo(21f, 9.28447f, 20.7076f, 8.65523f, 20.2008f, 8.22772f)
                lineTo(13.4508f, 2.53318f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsHome!!
    }

private var _FluentuiSystemIconsHome: ImageVector? = null