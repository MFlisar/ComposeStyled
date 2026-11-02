package com.michaelflisar.composestyled.material3.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.foundation.components.FoundationBadgeLayout
import com.michaelflisar.composestyled.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.ui.components.StyledBadge

class Material3Badge internal constructor() : StyledBadge.Implementation {

    override val defaults: StyledBadge.Defaults
        @Composable get() = StyledBadge.Defaults(
            minSize = 16.dp,
            dotSize = 6.dp,
            textStyle = StyledTheme.typography.labelSmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
            iconSize = 10.dp,
            contentPadding = PaddingValues(horizontal = 4.dp),
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3Badge() }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        badge: (@Composable () -> Unit)?,
        content: @Composable () -> Unit,
        alignment: Alignment,
        minSize: Dp,
        dotSize: Dp,
        textStyle: TextStyle,
        contentPadding: PaddingValues,
        modifier: Modifier,
        offset: DpOffset,
    ) {
        val size = if (badge == null) dotSize else minSize
        FoundationBadgeLayout(
            modifier = modifier,
            alignment = alignment,
            offset = offset,
            content = content,
            badge = {
                Box(
                    modifier = Modifier.defaultMinSize(minWidth = size, minHeight = size),
                    contentAlignment = Alignment.Center,
                ) {
                    // Separate the native container from its fixed padding so badge defaults stay adjustable.
                    Badge(modifier = Modifier.matchParentSize())
                    if (badge != null) {
                        Box(Modifier.padding(contentPadding)) {
                            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onError) {
                                ProvideTextStyle(textStyle) {
                                    CopyMaterialThemeLocals(
                                        contentColor = LocalContentColor.current,
                                        content = badge,
                                    )
                                }
                            }
                        }
                    }
                }
            },
        )
    }
}
