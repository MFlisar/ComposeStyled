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

val FluentuiSystemIconsLineHorizontal3: ImageVector
    get() {
        if (_FluentuiSystemIconsLineHorizontal3 != null) return _FluentuiSystemIconsLineHorizontal3!!

        _FluentuiSystemIconsLineHorizontal3 = ImageVector.Builder(
            name = "line-horizontal-3",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black)
            ) {
                moveTo(2.75281f, 17.9997f)
                horizontalLineTo(21.2528f)
                curveTo(21.667f, 17.9997f, 22.0028f, 18.3355f, 22.0028f, 18.7497f)
                curveTo(22.0028f, 19.1294f, 21.7207f, 19.4432f, 21.3546f, 19.4928f)
                lineTo(21.2528f, 19.4997f)
                horizontalLineTo(2.75281f)
                curveTo(2.3386f, 19.4997f, 2.00281f, 19.1639f, 2.00281f, 18.7497f)
                curveTo(2.00281f, 18.37f, 2.28497f, 18.0562f, 2.65104f, 18.0065f)
                lineTo(2.75281f, 17.9997f)
                horizontalLineTo(21.2528f)
                horizontalLineTo(2.75281f)
                close()
                moveTo(2.75281f, 11.5027f)
                horizontalLineTo(21.2528f)
                curveTo(21.667f, 11.5027f, 22.0028f, 11.8385f, 22.0028f, 12.2527f)
                curveTo(22.0028f, 12.6324f, 21.7207f, 12.9462f, 21.3546f, 12.9959f)
                lineTo(21.2528f, 13.0027f)
                horizontalLineTo(2.75281f)
                curveTo(2.3386f, 13.0027f, 2.00281f, 12.6669f, 2.00281f, 12.2527f)
                curveTo(2.00281f, 11.873f, 2.28497f, 11.5592f, 2.65104f, 11.5095f)
                lineTo(2.75281f, 11.5027f)
                horizontalLineTo(21.2528f)
                horizontalLineTo(2.75281f)
                close()
                moveTo(2.75195f, 5.00293f)
                horizontalLineTo(21.252f)
                curveTo(21.6662f, 5.00293f, 22.002f, 5.33872f, 22.002f, 5.75293f)
                curveTo(22.002f, 6.13263f, 21.7198f, 6.44642f, 21.3537f, 6.49608f)
                lineTo(21.252f, 6.50293f)
                horizontalLineTo(2.75195f)
                curveTo(2.33774f, 6.50293f, 2.00195f, 6.16714f, 2.00195f, 5.75293f)
                curveTo(2.00195f, 5.37323f, 2.28411f, 5.05944f, 2.65018f, 5.00978f)
                lineTo(2.75195f, 5.00293f)
                horizontalLineTo(21.252f)
                horizontalLineTo(2.75195f)
                close()
            }
        }.build()

        return _FluentuiSystemIconsLineHorizontal3!!
    }

private var _FluentuiSystemIconsLineHorizontal3: ImageVector? = null