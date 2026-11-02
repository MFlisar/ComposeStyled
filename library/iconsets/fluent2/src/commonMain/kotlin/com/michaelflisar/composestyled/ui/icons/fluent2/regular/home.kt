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

val FluentuiSystemIconsHome: ImageVector
    get() {
        if (_FluentuiSystemIconsHome != null) return _FluentuiSystemIconsHome!!
        
        _FluentuiSystemIconsHome = ImageVector.Builder(
            name = "home",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(10.5495f, 2.53189f)
                curveTo(11.3874f, 1.82531f, 12.6126f, 1.82531f, 13.4505f, 2.5319f)
                lineTo(20.2005f, 8.224f)
                curveTo(20.7074f, 8.65152f, 21f, 9.2809f, 21f, 9.94406f)
                lineTo(21f, 19.2539f)
                curveTo(21f, 20.2204f, 20.2165f, 21.0039f, 19.25f, 21.0039f)
                horizontalLineTo(15.75f)
                curveTo(14.7835f, 21.0039f, 14f, 20.2204f, 14f, 19.2539f)
                lineTo(14f, 14.2468f)
                curveTo(14f, 14.1088f, 13.8881f, 13.9968f, 13.75f, 13.9968f)
                horizontalLineTo(10.25f)
                curveTo(10.1119f, 13.9968f, 9.99999f, 14.1088f, 9.99999f, 14.2468f)
                lineTo(9.99999f, 19.2539f)
                curveTo(9.99999f, 20.2204f, 9.2165f, 21.0039f, 8.25f, 21.0039f)
                horizontalLineTo(4.75f)
                curveTo(3.7835f, 21.0039f, 3f, 20.2204f, 3f, 19.2539f)
                verticalLineTo(9.94406f)
                curveTo(3f, 9.2809f, 3.29255f, 8.65152f, 3.79952f, 8.224f)
                lineTo(10.5495f, 2.53189f)
                close()
                moveTo(12.4835f, 3.6786f)
                curveTo(12.2042f, 3.44307f, 11.7958f, 3.44307f, 11.5165f, 3.6786f)
                lineTo(4.76651f, 9.37071f)
                curveTo(4.59752f, 9.51321f, 4.5f, 9.72301f, 4.5f, 9.94406f)
                lineTo(4.5f, 19.2539f)
                curveTo(4.5f, 19.392f, 4.61193f, 19.5039f, 4.75f, 19.5039f)
                horizontalLineTo(8.25f)
                curveTo(8.38807f, 19.5039f, 8.49999f, 19.392f, 8.49999f, 19.2539f)
                lineTo(8.49999f, 14.2468f)
                curveTo(8.49999f, 13.2803f, 9.2835f, 12.4968f, 10.25f, 12.4968f)
                horizontalLineTo(13.75f)
                curveTo(14.7165f, 12.4968f, 15.5f, 13.2803f, 15.5f, 14.2468f)
                lineTo(15.5f, 19.2539f)
                curveTo(15.5f, 19.392f, 15.6119f, 19.5039f, 15.75f, 19.5039f)
                horizontalLineTo(19.25f)
                curveTo(19.3881f, 19.5039f, 19.5f, 19.392f, 19.5f, 19.2539f)
                lineTo(19.5f, 9.94406f)
                curveTo(19.5f, 9.72301f, 19.4025f, 9.51321f, 19.2335f, 9.37071f)
                lineTo(12.4835f, 3.6786f)
                close()
            }
        }.build()
        
        return _FluentuiSystemIconsHome!!
    }

private var _FluentuiSystemIconsHome: ImageVector? = null