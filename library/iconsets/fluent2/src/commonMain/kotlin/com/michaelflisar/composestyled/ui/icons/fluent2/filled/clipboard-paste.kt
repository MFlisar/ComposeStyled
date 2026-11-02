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

val FluentuiSystemIconsClipboardPaste: ImageVector
    get() {
        if (_FluentuiSystemIconsClipboardPaste != null) return _FluentuiSystemIconsClipboardPaste!!
        
        _FluentuiSystemIconsClipboardPaste = ImageVector.Builder(
            name = "clipboard-paste",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(12.7533f, 2f)
                curveTo(13.9109f, 2f, 14.8641f, 2.87549f, 14.9867f, 4.00046f)
                lineTo(16.75f, 4f)
                curveTo(17.9409f, 4f, 18.9156f, 4.92516f, 18.9948f, 6.09595f)
                lineTo(19f, 6.25f)
                curveTo(19f, 6.62919f, 18.7182f, 6.94257f, 18.3526f, 6.99216f)
                lineTo(18.249f, 6.999f)
                curveTo(17.8698f, 6.999f, 17.5564f, 6.71722f, 17.5068f, 6.35163f)
                lineTo(17.5f, 6.25f)
                curveTo(17.5f, 5.8703f, 17.2178f, 5.55651f, 16.8518f, 5.50685f)
                lineTo(16.75f, 5.5f)
                lineTo(14.6176f, 5.50082f)
                curveTo(14.2141f, 6.09953f, 13.5297f, 6.4933f, 12.7533f, 6.4933f)
                horizontalLineTo(9.24665f)
                curveTo(8.47031f, 6.4933f, 7.78594f, 6.09953f, 7.38237f, 5.50082f)
                lineTo(5.25f, 5.5f)
                curveTo(4.8703f, 5.5f, 4.55651f, 5.78215f, 4.50685f, 6.14823f)
                lineTo(4.5f, 6.25f)
                verticalLineTo(19.7546f)
                curveTo(4.5f, 20.1343f, 4.78215f, 20.4481f, 5.14823f, 20.4977f)
                lineTo(5.25f, 20.5046f)
                lineTo(8.25f, 20.5041f)
                curveTo(8.62964f, 20.5041f, 8.94343f, 20.7861f, 8.99314f, 21.1521f)
                lineTo(9f, 21.254f)
                curveTo(9f, 21.6682f, 8.66423f, 22.0041f, 8.25f, 22.0041f)
                lineTo(5.25f, 22.0046f)
                curveTo(4.05914f, 22.0046f, 3.08436f, 21.0794f, 3.00519f, 19.9086f)
                lineTo(3f, 19.7546f)
                verticalLineTo(6.25f)
                curveTo(3f, 5.05914f, 3.92516f, 4.08436f, 5.09595f, 4.00519f)
                lineTo(5.25f, 4f)
                lineTo(7.01333f, 4.00046f)
                curveTo(7.13595f, 2.87549f, 8.08906f, 2f, 9.24665f, 2f)
                horizontalLineTo(12.7533f)
                close()
                moveTo(18.75f, 8f)
                curveTo(19.9409f, 8f, 20.9156f, 8.92516f, 20.9948f, 10.096f)
                lineTo(21f, 10.25f)
                verticalLineTo(19.75f)
                curveTo(21f, 20.9409f, 20.0748f, 21.9156f, 18.904f, 21.9948f)
                lineTo(18.75f, 22f)
                horizontalLineTo(12.25f)
                curveTo(11.0591f, 22f, 10.0844f, 21.0748f, 10.0052f, 19.904f)
                lineTo(10f, 19.75f)
                verticalLineTo(10.25f)
                curveTo(10f, 9.05914f, 10.9252f, 8.08436f, 12.096f, 8.00519f)
                lineTo(12.25f, 8f)
                horizontalLineTo(18.75f)
                close()
                moveTo(12.7533f, 3.5f)
                horizontalLineTo(9.24665f)
                curveTo(8.83429f, 3.5f, 8.5f, 3.83429f, 8.5f, 4.24665f)
                curveTo(8.5f, 4.65902f, 8.83429f, 4.9933f, 9.24665f, 4.9933f)
                horizontalLineTo(12.7533f)
                curveTo(13.1657f, 4.9933f, 13.5f, 4.65902f, 13.5f, 4.24665f)
                curveTo(13.5f, 3.83429f, 13.1657f, 3.5f, 12.7533f, 3.5f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsClipboardPaste!!
    }

private var _FluentuiSystemIconsClipboardPaste: ImageVector? = null