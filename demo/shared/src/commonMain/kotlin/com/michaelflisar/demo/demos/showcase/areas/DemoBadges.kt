package com.michaelflisar.demo.demos.showcase.areas

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.michaelflisar.composestyled.UpdateStyledCustomization
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.components.StyledBadge
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.utils.ExpandState
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle

@Composable
internal fun DemoBadges(baseId: Int, expandState: ExpandState) {
    RegionContainer(
        title = "Badges",
        id = baseId + 1,
        expanded = expandState,
    ) {
        SubTitle("Counter centered on the corner; outer padding reserves clearance")
        StyledBadge(
            badge = { StyledText("3") },
            content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
            modifier = Modifier.padding(12.dp),
        )

        SubTitle("Offset toward end and upward")
        StyledBadge(
            badge = { StyledText("3") },
            content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
            offset = DpOffset(2.dp, (-2).dp),
            modifier = Modifier.padding(14.dp),
        )

        SubTitle("Text labels on text content")
        StyledBadge(
            badge = { StyledText("NEW") },
            content = { StyledText("Inbox") },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
        )
        StyledBadge(
            badge = { StyledText("99+") },
            content = { StyledText("Notifications") },
            alignment = Alignment.BottomEnd,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
        )

        SubTitle("Dot with custom alignment")
        StyledBadge(
            badge = null,
            content = { StyledIcon(icon = Icons.Default.Add.toIconComposable()) },
            alignment = Alignment.BottomEnd,
            modifier = Modifier.padding(4.dp),
        )

        SubTitle("Pulsing notification dot")
        val badgePulse = rememberInfiniteTransition(label = "Badge pulse").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(700, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "Badge dot size",
        )
        StyledBadge(
            badge = null,
            content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
            dotSize = 6.dp + 6.dp * badgePulse.value,
            modifier = Modifier.padding(14.dp),
        )

        SubTitle("Icon inherits badge size; anchor keeps its normal size")
        StyledBadge(
            badge = { StyledIcon(icon = Icons.Default.Add.toIconComposable()) },
            content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
            modifier = Modifier.padding(12.dp),
        )

        SubTitle("Explicit icon size")
        StyledBadge(
            badge = { StyledIcon(icon = Icons.Default.Add.toIconComposable(), size = 12.dp) },
            content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
            modifier = Modifier.padding(12.dp),
        )

        SubTitle("Customized defaults")
        UpdateStyledCustomization(
            update = {
                update(StyledBadge.DefaultsKey) {
                    copy(
                        minSize = 20.dp,
                        iconSize = 12.dp,
                        textStyle = textStyle.copy(fontSize = 12.sp, lineHeight = 18.sp),
                    )
                }
            },
        ) {
            StyledBadge(
                badge = { StyledText("99+") },
                content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            )
            StyledBadge(
                badge = { StyledIcon(icon = Icons.Default.Add.toIconComposable()) },
                content = { StyledIcon(icon = Icons.Default.Notifications.toIconComposable()) },
                modifier = Modifier.padding(12.dp),
            )
        }
    }
}
