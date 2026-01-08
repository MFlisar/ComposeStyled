package com.michaelflisar.composestyled.theme.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.core.components.StyledCardConfig
import com.michaelflisar.composestyled.core.components.StyledCardDefaults
import com.michaelflisar.composestyled.core.runtime.ProvideStyledLocals
import com.michaelflisar.composestyled.core.theme.StyledTheme

@Composable
internal fun StyledCardImpl(
    modifier: Modifier,
    shape: Shape,
    config: StyledCardConfig,
    contentPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit,
) {
    ProvideStyledLocals(
        contentColor = config.contentColor,
        backgroundColor = config.containerColor,
    ) {
        Box(
            modifier
                .clip(shape)
                .background(config.containerColor)
                .then(
                    when (config.variant) {
                        StyledCardConfig.Variant.Outlined -> Modifier.border(
                            width = Dp.Hairline,
                            color = config.borderColor,
                            shape = shape
                        )

                        StyledCardConfig.Variant.Filled -> Modifier
                    }
                )
                .padding(contentPadding)
        ) {
            Column(content = content)
        }
    }
}