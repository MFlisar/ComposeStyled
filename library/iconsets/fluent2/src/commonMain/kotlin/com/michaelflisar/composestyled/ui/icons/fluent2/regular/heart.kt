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

val FluentuiSystemIconsHeart: ImageVector
    get() {
        if (_FluentuiSystemIconsHeart != null) return _FluentuiSystemIconsHeart!!
        
        _FluentuiSystemIconsHeart = ImageVector.Builder(
            name = "heart",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(12.8199f, 5.57912f)
                lineTo(11.9992f, 6.40163f)
                lineTo(11.1759f, 5.57838f)
                curveTo(9.07688f, 3.47931f, 5.67361f, 3.47931f, 3.57455f, 5.57838f)
                curveTo(1.47548f, 7.67744f, 1.47548f, 11.0807f, 3.57455f, 13.1798f)
                lineTo(11.4699f, 21.0751f)
                curveTo(11.7628f, 21.368f, 12.2377f, 21.368f, 12.5306f, 21.0751f)
                lineTo(20.432f, 13.1783f)
                curveTo(22.5264f, 11.0723f, 22.53f, 7.67857f, 20.4306f, 5.57912f)
                curveTo(18.3277f, 3.47623f, 14.9228f, 3.47623f, 12.8199f, 5.57912f)
                close()
                moveTo(19.3684f, 12.1206f)
                lineTo(12.0002f, 19.4842f)
                lineTo(4.63521f, 12.1191f)
                curveTo(3.12192f, 10.6058f, 3.12192f, 8.15232f, 4.63521f, 6.63904f)
                curveTo(6.14849f, 5.12575f, 8.602f, 5.12575f, 10.1153f, 6.63904f)
                lineTo(11.4727f, 7.99648f)
                curveTo(11.7706f, 8.29435f, 12.2553f, 8.28854f, 12.5459f, 7.98363f)
                lineTo(13.8806f, 6.63978f)
                curveTo(15.3977f, 5.12268f, 17.8528f, 5.12268f, 19.3699f, 6.63978f)
                curveTo(20.8836f, 8.15343f, 20.881f, 10.5997f, 19.3684f, 12.1206f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsHeart!!
    }

private var _FluentuiSystemIconsHeart: ImageVector? = null