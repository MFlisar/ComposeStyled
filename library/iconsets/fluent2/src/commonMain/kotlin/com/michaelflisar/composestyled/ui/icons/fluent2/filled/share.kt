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

val FluentuiSystemIconsShare: ImageVector
    get() {
        if (_FluentuiSystemIconsShare != null) return _FluentuiSystemIconsShare!!
        
        _FluentuiSystemIconsShare = ImageVector.Builder(
            name = "share",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(10.25f, 2.99985f)
                curveTo(10.6642f, 2.99985f, 10.9999f, 3.33571f, 11f, 3.74985f)
                curveTo(11f, 4.16407f, 10.6642f, 4.49985f, 10.25f, 4.49985f)
                horizontalLineTo(6.75f)
                curveTo(5.50741f, 4.49985f, 4.50008f, 5.50728f, 4.5f, 6.74985f)
                verticalLineTo(17.2499f)
                lineTo(4.51172f, 17.4803f)
                curveTo(4.62714f, 18.6147f, 5.58517f, 19.4999f, 6.75f, 19.4999f)
                horizontalLineTo(17.25f)
                curveTo(18.4926f, 19.4999f, 19.5f, 18.4925f, 19.5f, 17.2499f)
                verticalLineTo(15.2499f)
                curveTo(19.5001f, 14.8357f, 19.8358f, 14.4999f, 20.25f, 14.4999f)
                curveTo(20.6642f, 14.4999f, 20.9999f, 14.8357f, 21f, 15.2499f)
                verticalLineTo(17.2499f)
                curveTo(21f, 19.3209f, 19.3211f, 20.9999f, 17.25f, 20.9999f)
                horizontalLineTo(6.75f)
                curveTo(4.74378f, 20.9999f, 3.10551f, 19.4244f, 3.00488f, 17.4432f)
                lineTo(3f, 17.2499f)
                verticalLineTo(6.74985f)
                curveTo(3.00008f, 4.67885f, 4.67898f, 2.99985f, 6.75f, 2.99985f)
                horizontalLineTo(10.25f)
                close()
                moveTo(14.9365f, 2.06821f)
                curveTo(15.2026f, 1.94588f, 15.5159f, 1.98997f, 15.7383f, 2.18052f)
                lineTo(22.7383f, 8.18052f)
                curveTo(22.8986f, 8.31798f, 22.9938f, 8.5173f, 23f, 8.72837f)
                curveTo(23.006f, 8.93935f, 22.9224f, 9.14336f, 22.7705f, 9.28989f)
                lineTo(15.7705f, 16.0399f)
                curveTo(15.554f, 16.2486f, 15.2338f, 16.3077f, 14.957f, 16.1903f)
                curveTo(14.6802f, 16.0726f, 14.5f, 15.8007f, 14.5f, 15.4999f)
                verticalLineTo(12.5243f)
                curveTo(13.4857f, 12.5878f, 12.5295f, 12.7971f, 11.5605f, 13.2928f)
                curveTo(10.4241f, 13.8742f, 9.21642f, 14.8744f, 7.87109f, 16.5956f)
                lineTo(7.59961f, 16.9501f)
                curveTo(7.40588f, 17.2081f, 7.06883f, 17.3138f, 6.7627f, 17.2118f)
                curveTo(6.45646f, 17.1097f, 6.25f, 16.8227f, 6.25f, 16.4999f)
                curveTo(6.25002f, 13.6426f, 6.93659f, 10.9108f, 8.41797f, 8.87192f)
                curveTo(9.7938f, 6.97863f, 11.8288f, 5.72528f, 14.5f, 5.52817f)
                verticalLineTo(2.74985f)
                lineTo(14.5078f, 2.64146f)
                curveTo(14.5441f, 2.39271f, 14.7037f, 2.17528f, 14.9365f, 2.06821f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsShare!!
    }

private var _FluentuiSystemIconsShare: ImageVector? = null