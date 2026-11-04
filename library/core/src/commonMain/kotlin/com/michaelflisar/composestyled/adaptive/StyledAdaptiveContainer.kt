package com.michaelflisar.composestyled.adaptive

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.platform.Platform

/**
 * Provides adaptive information resolved for this container's available layout bounds.
 */
@Composable
fun StyledAdaptiveContainer(
    modifier: Modifier = Modifier,
    platform: Platform = StyledTheme.platform,
    policy: StyledAdaptivePolicy = StyledAdaptive.DefaultPolicy,
    content: @Composable (State<StyledAdaptiveInfo>) -> Unit,
) {
    BoxWithConstraints(modifier) {
        val layoutSize = rememberUpdatedState(StyledAdaptiveSize(maxWidth, maxHeight))
        val adaptiveInfo = StyledAdaptive.info(
            platform = platform,
            layoutSize = layoutSize,
            policy = policy,
        )
        content(adaptiveInfo)
    }
}
