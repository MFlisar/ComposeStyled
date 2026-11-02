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

val FluentuiSystemIconsArrowSync: ImageVector
    get() {
        if (_FluentuiSystemIconsArrowSync != null) return _FluentuiSystemIconsArrowSync!!
        
        _FluentuiSystemIconsArrowSync = ImageVector.Builder(
            name = "arrow-sync",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(16.0518f, 5.0285f)
                curveTo(15.7169f, 5.46765f, 15.8014f, 6.09515f, 16.2405f, 6.43007f)
                curveTo(17.9675f, 7.74714f, 19f, 9.78703f, 19f, 12f)
                curveTo(19f, 15.4973f, 16.4352f, 18.3956f, 13.084f, 18.9166f)
                lineTo(13.7929f, 18.2071f)
                curveTo(14.1834f, 17.8166f, 14.1834f, 17.1834f, 13.7929f, 16.7929f)
                curveTo(13.4024f, 16.4024f, 12.7692f, 16.4024f, 12.3787f, 16.7929f)
                lineTo(9.87868f, 19.2929f)
                curveTo(9.48816f, 19.6834f, 9.48816f, 20.3166f, 9.87868f, 20.7071f)
                lineTo(12.3787f, 23.2071f)
                curveTo(12.7692f, 23.5976f, 13.4024f, 23.5976f, 13.7929f, 23.2071f)
                curveTo(14.1834f, 22.8166f, 14.1834f, 22.1834f, 13.7929f, 21.7929f)
                lineTo(12.9497f, 20.9505f)
                curveTo(17.4739f, 20.476f, 21f, 16.6498f, 21f, 12f)
                curveTo(21f, 9.15644f, 19.6712f, 6.53122f, 17.4533f, 4.83978f)
                curveTo(17.0142f, 4.50486f, 16.3867f, 4.58936f, 16.0518f, 5.0285f)
                close()
                moveTo(14.1213f, 3.29289f)
                lineTo(11.6213f, 0.792893f)
                curveTo(11.2308f, 0.402369f, 10.5976f, 0.402369f, 10.2071f, 0.792893f)
                curveTo(9.84662f, 1.15338f, 9.81889f, 1.72061f, 10.1239f, 2.1129f)
                lineTo(10.2071f, 2.20711f)
                lineTo(11.0503f, 3.04951f)
                curveTo(6.52615f, 3.52399f, 3f, 7.35021f, 3f, 12f)
                curveTo(3f, 14.7198f, 4.21515f, 17.2432f, 6.2716f, 18.9419f)
                curveTo(6.6974f, 19.2936f, 7.32771f, 19.2335f, 7.67943f, 18.8077f)
                curveTo(8.03116f, 18.3819f, 7.97111f, 17.7516f, 7.54531f, 17.3999f)
                curveTo(5.94404f, 16.0772f, 5f, 14.1168f, 5f, 12f)
                curveTo(5f, 8.50269f, 7.56475f, 5.60441f, 10.916f, 5.08343f)
                lineTo(10.2071f, 5.79289f)
                curveTo(9.81658f, 6.18342f, 9.81658f, 6.81658f, 10.2071f, 7.20711f)
                curveTo(10.5676f, 7.56759f, 11.1348f, 7.59532f, 11.5271f, 7.2903f)
                lineTo(11.6213f, 7.20711f)
                lineTo(14.1213f, 4.70711f)
                curveTo(14.4818f, 4.34662f, 14.5095f, 3.77939f, 14.2045f, 3.3871f)
                lineTo(14.1213f, 3.29289f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsArrowSync!!
    }

private var _FluentuiSystemIconsArrowSync: ImageVector? = null