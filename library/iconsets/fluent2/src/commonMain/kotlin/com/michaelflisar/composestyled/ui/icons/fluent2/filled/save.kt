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

val FluentuiSystemIconsSave: ImageVector
    get() {
        if (_FluentuiSystemIconsSave != null) return _FluentuiSystemIconsSave!!
        
        _FluentuiSystemIconsSave = ImageVector.Builder(
            name = "save",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(6.75f, 3f)
                horizontalLineTo(5.75f)
                curveTo(4.23122f, 3f, 3f, 4.23122f, 3f, 5.75f)
                verticalLineTo(18.25f)
                curveTo(3f, 19.7688f, 4.23122f, 21f, 5.75f, 21f)
                horizontalLineTo(6f)
                verticalLineTo(15f)
                curveTo(6f, 13.7574f, 7.00736f, 12.75f, 8.25f, 12.75f)
                horizontalLineTo(15.75f)
                curveTo(16.9926f, 12.75f, 18f, 13.7574f, 18f, 15f)
                verticalLineTo(21f)
                horizontalLineTo(18.25f)
                curveTo(19.7688f, 21f, 21f, 19.7688f, 21f, 18.25f)
                verticalLineTo(8.28553f)
                curveTo(21f, 7.42358f, 20.6576f, 6.59693f, 20.0481f, 5.98744f)
                lineTo(18.0126f, 3.9519f)
                curveTo(17.4114f, 3.35079f, 16.5991f, 3.00947f, 15.75f, 3.00019f)
                verticalLineTo(7.5f)
                curveTo(15.75f, 8.74264f, 14.7426f, 9.75f, 13.5f, 9.75f)
                horizontalLineTo(9f)
                curveTo(7.75736f, 9.75f, 6.75f, 8.74264f, 6.75f, 7.5f)
                verticalLineTo(3f)
                close()
                moveTo(14.25f, 3f)
                verticalLineTo(7.5f)
                curveTo(14.25f, 7.91421f, 13.9142f, 8.25f, 13.5f, 8.25f)
                horizontalLineTo(9f)
                curveTo(8.58579f, 8.25f, 8.25f, 7.91421f, 8.25f, 7.5f)
                verticalLineTo(3f)
                horizontalLineTo(14.25f)
                close()
                moveTo(16.5f, 21f)
                verticalLineTo(15f)
                curveTo(16.5f, 14.5858f, 16.1642f, 14.25f, 15.75f, 14.25f)
                horizontalLineTo(8.25f)
                curveTo(7.83579f, 14.25f, 7.5f, 14.5858f, 7.5f, 15f)
                verticalLineTo(21f)
                horizontalLineTo(16.5f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsSave!!
    }

private var _FluentuiSystemIconsSave: ImageVector? = null