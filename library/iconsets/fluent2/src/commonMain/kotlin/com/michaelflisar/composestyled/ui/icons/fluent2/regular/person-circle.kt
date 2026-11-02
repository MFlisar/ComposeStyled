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

val FluentuiSystemIconsPersonCircle: ImageVector
    get() {
        if (_FluentuiSystemIconsPersonCircle != null) return _FluentuiSystemIconsPersonCircle!!
        
        _FluentuiSystemIconsPersonCircle = ImageVector.Builder(
            name = "person-circle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(17f, 13.5f)
                curveTo(17f, 12.6716f, 16.3284f, 12f, 15.5f, 12f)
                horizontalLineTo(8.5f)
                curveTo(7.67157f, 12f, 7f, 12.6716f, 7f, 13.5f)
                verticalLineTo(14f)
                curveTo(7f, 15.9714f, 8.85951f, 18f, 12f, 18f)
                curveTo(15.1405f, 18f, 17f, 15.9714f, 17f, 14f)
                verticalLineTo(13.5f)
                close()
                moveTo(14.75f, 8.25f)
                curveTo(14.75f, 6.73122f, 13.5188f, 5.5f, 12f, 5.5f)
                curveTo(10.4812f, 5.5f, 9.25f, 6.73122f, 9.25f, 8.25f)
                curveTo(9.25f, 9.76878f, 10.4812f, 11f, 12f, 11f)
                curveTo(13.5188f, 11f, 14.75f, 9.76878f, 14.75f, 8.25f)
                close()
                moveTo(22f, 12f)
                curveTo(22f, 17.5228f, 17.5228f, 22f, 12f, 22f)
                curveTo(6.47715f, 22f, 2f, 17.5228f, 2f, 12f)
                curveTo(2f, 6.47715f, 6.47715f, 2f, 12f, 2f)
                curveTo(17.5228f, 2f, 22f, 6.47715f, 22f, 12f)
                close()
                moveTo(20.5f, 12f)
                curveTo(20.5f, 7.30558f, 16.6944f, 3.5f, 12f, 3.5f)
                curveTo(7.30558f, 3.5f, 3.5f, 7.30558f, 3.5f, 12f)
                curveTo(3.5f, 16.6944f, 7.30558f, 20.5f, 12f, 20.5f)
                curveTo(16.6944f, 20.5f, 20.5f, 16.6944f, 20.5f, 12f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsPersonCircle!!
    }

private var _FluentuiSystemIconsPersonCircle: ImageVector? = null