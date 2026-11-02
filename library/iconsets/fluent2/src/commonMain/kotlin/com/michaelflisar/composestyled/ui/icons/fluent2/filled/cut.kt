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

val FluentuiSystemIconsCut: ImageVector
    get() {
        if (_FluentuiSystemIconsCut != null) return _FluentuiSystemIconsCut!!
        
        _FluentuiSystemIconsCut = ImageVector.Builder(
            name = "cut",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(7.8294f, 2.43943f)
                curveTo(7.52083f, 1.98139f, 6.89937f, 1.86022f, 6.44132f, 2.16879f)
                curveTo(5.98328f, 2.47736f, 5.86211f, 3.09882f, 6.17068f, 3.55686f)
                lineTo(10.9634f, 10.6711f)
                lineTo(8.63676f, 14.3487f)
                curveTo(8.13715f, 14.1244f, 7.58315f, 13.9996f, 7f, 13.9996f)
                curveTo(4.79086f, 13.9996f, 3f, 15.7905f, 3f, 17.9996f)
                curveTo(3f, 20.2087f, 4.79086f, 21.9996f, 7f, 21.9996f)
                curveTo(9.20914f, 21.9996f, 11f, 20.2087f, 11f, 17.9996f)
                curveTo(11f, 17.1029f, 10.705f, 16.2752f, 10.2067f, 15.6081f)
                lineTo(12.1839f, 12.4828f)
                lineTo(14.0676f, 15.2791f)
                curveTo(13.4051f, 15.9929f, 13f, 16.9489f, 13f, 17.9996f)
                curveTo(13f, 20.2087f, 14.7909f, 21.9996f, 17f, 21.9996f)
                curveTo(19.2091f, 21.9996f, 21f, 20.2087f, 21f, 17.9996f)
                curveTo(21f, 15.7905f, 19.2091f, 13.9996f, 17f, 13.9996f)
                curveTo(16.5639f, 13.9996f, 16.1441f, 14.0694f, 15.7511f, 14.1984f)
                lineTo(12.1319f, 8.82411f)
                lineTo(12.1313f, 8.8251f)
                lineTo(7.8294f, 2.43943f)
                close()
                moveTo(5f, 17.9996f)
                curveTo(5f, 16.895f, 5.89543f, 15.9996f, 7f, 15.9996f)
                curveTo(8.10457f, 15.9996f, 9f, 16.895f, 9f, 17.9996f)
                curveTo(9f, 19.1042f, 8.10457f, 19.9996f, 7f, 19.9996f)
                curveTo(5.89543f, 19.9996f, 5f, 19.1042f, 5f, 17.9996f)
                close()
                moveTo(15f, 17.9996f)
                curveTo(15f, 16.895f, 15.8954f, 15.9996f, 17f, 15.9996f)
                curveTo(18.1046f, 15.9996f, 19f, 16.895f, 19f, 17.9996f)
                curveTo(19f, 19.1042f, 18.1046f, 19.9996f, 17f, 19.9996f)
                curveTo(15.8954f, 19.9996f, 15f, 19.1042f, 15f, 17.9996f)
                close()
                moveTo(14.5202f, 8.78988f)
                lineTo(17.8452f, 3.53421f)
                curveTo(18.1404f, 3.06749f, 18.0014f, 2.44977f, 17.5347f, 2.1545f)
                curveTo(17.068f, 1.85922f, 16.4503f, 1.99821f, 16.155f, 2.46494f)
                lineTo(13.2998f, 6.97801f)
                lineTo(14.5202f, 8.78988f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsCut!!
    }

private var _FluentuiSystemIconsCut: ImageVector? = null