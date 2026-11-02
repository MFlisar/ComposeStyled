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
                moveTo(17.2509f, 4f)
                lineTo(17.1491f, 4.00685f)
                curveTo(16.783f, 4.05651f, 16.5009f, 4.3703f, 16.5009f, 4.75f)
                lineTo(16.5f, 17.442f)
                lineTo(13.2801f, 14.2244f)
                lineTo(13.196f, 14.1518f)
                curveTo(12.9023f, 13.9341f, 12.4856f, 13.9585f, 12.2194f, 14.2248f)
                curveTo(11.9267f, 14.5179f, 11.9269f, 14.9927f, 12.2199f, 15.2855f)
                lineTo(16.7237f, 19.7855f)
                lineTo(16.8078f, 19.8581f)
                curveTo(17.1015f, 20.0758f, 17.5182f, 20.0514f, 17.7843f, 19.7851f)
                lineTo(22.2806f, 15.2851f)
                lineTo(22.3531f, 15.2009f)
                curveTo(22.5709f, 14.9072f, 22.5465f, 14.4905f, 22.2801f, 14.2244f)
                lineTo(22.196f, 14.1518f)
                curveTo(21.9023f, 13.9341f, 21.4856f, 13.9585f, 21.2194f, 14.2248f)
                lineTo(18f, 17.446f)
                lineTo(18.0009f, 4.75f)
                lineTo(17.994f, 4.64823f)
                curveTo(17.9444f, 4.28215f, 17.6306f, 4f, 17.2509f, 4f)
                close()
                moveTo(6.21441f, 4.21969f)
                lineTo(1.71965f, 8.71474f)
                lineTo(1.64704f, 8.79886f)
                curveTo(1.42919f, 9.09248f, 1.45341f, 9.50914f, 1.71969f, 9.7754f)
                lineTo(1.80381f, 9.84802f)
                curveTo(2.09743f, 10.0659f, 2.51409f, 10.0416f, 2.78035f, 9.77537f)
                lineTo(5.997f, 6.55711f)
                lineTo(5.99766f, 19.2549f)
                lineTo(6.00451f, 19.3567f)
                curveTo(6.05417f, 19.7228f, 6.36797f, 20.0049f, 6.74766f, 20.0049f)
                lineTo(6.84943f, 19.9981f)
                curveTo(7.21551f, 19.9484f, 7.49766f, 19.6346f, 7.49766f, 19.2549f)
                lineTo(7.497f, 6.55911f)
                lineTo(10.7199f, 9.77596f)
                lineTo(10.8041f, 9.84849f)
                curveTo(11.0979f, 10.066f, 11.5145f, 10.0414f, 11.7805f, 9.77481f)
                curveTo(12.0731f, 9.4816f, 12.0726f, 9.00672f, 11.7794f, 8.71415f)
                lineTo(7.27451f, 4.21909f)
                lineTo(7.19038f, 4.14661f)
                curveTo(6.89676f, 3.9292f, 6.48047f, 3.95361f, 6.21441f, 4.21969f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsArrowSort!!
    }

private var _FluentuiSystemIconsArrowSort: ImageVector? = null