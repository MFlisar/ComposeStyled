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

val FluentuiSystemIconsQuestionCircle: ImageVector
    get() {
        if (_FluentuiSystemIconsQuestionCircle != null) return _FluentuiSystemIconsQuestionCircle!!
        
        _FluentuiSystemIconsQuestionCircle = ImageVector.Builder(
            name = "question-circle",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(12f, 2f)
                curveTo(17.523f, 2f, 22f, 6.478f, 22f, 12f)
                curveTo(22f, 17.522f, 17.523f, 22f, 12f, 22f)
                curveTo(6.477f, 22f, 2f, 17.522f, 2f, 12f)
                curveTo(2f, 6.478f, 6.477f, 2f, 12f, 2f)
                close()
                moveTo(12f, 15.5f)
                curveTo(11.4477f, 15.5f, 11f, 15.9477f, 11f, 16.5f)
                curveTo(11f, 17.0523f, 11.4477f, 17.5f, 12f, 17.5f)
                curveTo(12.5523f, 17.5f, 13f, 17.0523f, 13f, 16.5f)
                curveTo(13f, 15.9477f, 12.5523f, 15.5f, 12f, 15.5f)
                close()
                moveTo(12f, 6.75f)
                curveTo(10.4812f, 6.75f, 9.25f, 7.98122f, 9.25f, 9.5f)
                curveTo(9.25f, 9.91421f, 9.58579f, 10.25f, 10f, 10.25f)
                curveTo(10.3797f, 10.25f, 10.6935f, 9.96785f, 10.7432f, 9.60177f)
                lineTo(10.75f, 9.5f)
                curveTo(10.75f, 8.80964f, 11.3096f, 8.25f, 12f, 8.25f)
                curveTo(12.6904f, 8.25f, 13.25f, 8.80964f, 13.25f, 9.5f)
                curveTo(13.25f, 10.0388f, 13.115f, 10.3053f, 12.6051f, 10.8322f)
                lineTo(12.4697f, 10.9697f)
                curveTo(11.5916f, 11.8478f, 11.25f, 12.4171f, 11.25f, 13.5f)
                curveTo(11.25f, 13.9142f, 11.5858f, 14.25f, 12f, 14.25f)
                curveTo(12.4142f, 14.25f, 12.75f, 13.9142f, 12.75f, 13.5f)
                curveTo(12.75f, 12.9612f, 12.885f, 12.6947f, 13.3949f, 12.1678f)
                lineTo(13.5303f, 12.0303f)
                curveTo(14.4084f, 11.1522f, 14.75f, 10.5829f, 14.75f, 9.5f)
                curveTo(14.75f, 7.98122f, 13.5188f, 6.75f, 12f, 6.75f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsQuestionCircle!!
    }

private var _FluentuiSystemIconsQuestionCircle: ImageVector? = null