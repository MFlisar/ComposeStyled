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

val FluentuiSystemIconsAlert: ImageVector
    get() {
        if (_FluentuiSystemIconsAlert != null) return _FluentuiSystemIconsAlert!!
        
        _FluentuiSystemIconsAlert = ImageVector.Builder(
            name = "alert",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(9.04173f, 19.003f)
                horizontalLineTo(14.9583f)
                curveTo(14.7196f, 20.4211f, 13.486f, 21.5015f, 12f, 21.5015f)
                curveTo(10.514f, 21.5015f, 9.28037f, 20.4211f, 9.04173f, 19.003f)
                close()
                moveTo(12f, 2.00391f)
                curveTo(16.1421f, 2.00391f, 19.5f, 5.36177f, 19.5f, 9.50391f)
                verticalLineTo(13.5024f)
                lineTo(20.9183f, 16.6625f)
                curveTo(20.9732f, 16.7848f, 21.0016f, 16.9174f, 21.0016f, 17.0515f)
                curveTo(21.0016f, 17.5762f, 20.5763f, 18.0015f, 20.0516f, 18.0015f)
                horizontalLineTo(3.95219f)
                curveTo(3.8184f, 18.0015f, 3.68613f, 17.9733f, 3.56402f, 17.9186f)
                curveTo(3.08515f, 17.7042f, 2.87073f, 17.1422f, 3.08511f, 16.6633f)
                lineTo(4.50001f, 13.5028f)
                lineTo(4.50011f, 9.49099f)
                lineTo(4.50453f, 9.24107f)
                curveTo(4.6436f, 5.21035f, 7.95588f, 2.00391f, 12f, 2.00391f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsAlert!!
    }

private var _FluentuiSystemIconsAlert: ImageVector? = null