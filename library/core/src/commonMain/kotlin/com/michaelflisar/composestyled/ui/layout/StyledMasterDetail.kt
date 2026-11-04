package com.michaelflisar.composestyled.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.adaptive.StyledAdaptiveMasterDetailMode

/**
 * Arranges master and detail content in one pane or side by side.
 *
 * When [detailData] is null, [master] is shown in [StyledAdaptiveMasterDetailMode.SinglePane].
 * In [StyledAdaptiveMasterDetailMode.SideBySide], both slots are composed and [detail] receives
 * the nullable [detailData] so the caller can render its empty state.
 */
@Composable
fun <T : Any> StyledMasterDetail(
    modifier: Modifier = Modifier,
    mode: StyledAdaptiveMasterDetailMode,
    detailData: T?,
    masterFraction: Float = 0.4f,
    paneSpacing: Dp = StyledTheme.spacing.medium,
    master: @Composable () -> Unit,
    detail: @Composable (T?) -> Unit,
) {
    require(masterFraction > 0f && masterFraction < 1f) {
        "masterFraction must be greater than 0 and less than 1"
    }
    require(paneSpacing >= 0.dp && paneSpacing != Dp.Infinity) {
        "paneSpacing must be finite and non-negative"
    }

    when (mode) {
        StyledAdaptiveMasterDetailMode.SinglePane -> Box(modifier.fillMaxSize()) {
            if (detailData == null) {
                master()
            } else {
                detail(detailData)
            }
        }

        StyledAdaptiveMasterDetailMode.SideBySide -> Row(modifier.fillMaxSize()) {
            Box(Modifier.weight(masterFraction).fillMaxSize()) {
                master()
            }
            Spacer(Modifier.width(paneSpacing))
            Box(Modifier.weight(1f - masterFraction).fillMaxSize()) {
                detail(detailData)
            }
        }
    }
}
