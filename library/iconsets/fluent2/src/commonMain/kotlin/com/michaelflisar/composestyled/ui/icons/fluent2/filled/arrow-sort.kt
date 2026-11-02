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

val FluentuiSystemIconsArrowSort: ImageVector
    get() {
        if (_FluentuiSystemIconsArrowSort != null) return _FluentuiSystemIconsArrowSort!!
        
        _FluentuiSystemIconsArrowSort = ImageVector.Builder(
            name = "arrow-sort",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(6.28829f, 4.29327f)
                lineTo(2.29252f, 8.29327f)
                lineTo(2.20938f, 8.38752f)
                curveTo(1.90456f, 8.77997f, 1.93259f, 9.34719f, 2.29327f, 9.70748f)
                lineTo(2.38752f, 9.79062f)
                curveTo(2.77997f, 10.0954f, 3.34719f, 10.0674f, 3.70748f, 9.70673f)
                lineTo(6.001f, 7.41f)
                lineTo(6.00101f, 19.0007f)
                lineTo(6.00774f, 19.1173f)
                curveTo(6.0655f, 19.6146f, 6.48817f, 20.0007f, 7.00101f, 20.0007f)
                lineTo(7.11763f, 19.9939f)
                curveTo(7.61497f, 19.9362f, 8.00101f, 19.5135f, 8.00101f, 19.0007f)
                lineTo(8.001f, 7.417f)
                lineTo(10.2944f, 9.70757f)
                lineTo(10.3886f, 9.7907f)
                curveTo(10.7811f, 10.0955f, 11.3483f, 10.0674f, 11.7086f, 9.70664f)
                curveTo(12.0988f, 9.31586f, 12.0984f, 8.6827f, 11.7077f, 8.29243f)
                lineTo(7.70242f, 4.29243f)
                lineTo(7.60816f, 4.20931f)
                curveTo(7.21571f, 3.90456f, 6.64855f, 3.93262f, 6.28829f, 4.29327f)
                close()
                moveTo(17f, 4.00317f)
                lineTo(16.8834f, 4.0099f)
                curveTo(16.386f, 4.06767f, 16f, 4.49034f, 16f, 5.00317f)
                lineTo(16f, 16.583f)
                lineTo(13.7068f, 14.2925f)
                lineTo(13.6125f, 14.2094f)
                curveTo(13.2201f, 13.9045f, 12.6529f, 13.9325f, 12.2926f, 14.2932f)
                curveTo(11.9022f, 14.6839f, 11.9025f, 15.3171f, 12.2932f, 15.7074f)
                lineTo(16.297f, 19.7074f)
                lineTo(16.3912f, 19.7905f)
                curveTo(16.7837f, 20.0954f, 17.3509f, 20.0674f, 17.7112f, 19.7067f)
                lineTo(21.7074f, 15.7067f)
                lineTo(21.7906f, 15.6125f)
                curveTo(22.0954f, 15.22f, 22.0674f, 14.6528f, 21.7068f, 14.2925f)
                lineTo(21.6125f, 14.2094f)
                curveTo(21.2201f, 13.9045f, 20.6529f, 13.9325f, 20.2926f, 14.2932f)
                lineTo(18f, 16.587f)
                lineTo(18f, 5.00317f)
                lineTo(17.9933f, 4.88655f)
                curveTo(17.9355f, 4.38921f, 17.5128f, 4.00317f, 17f, 4.00317f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsArrowSort!!
    }

private var _FluentuiSystemIconsArrowSort: ImageVector? = null