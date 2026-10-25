package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.material3.CopyMaterialThemeLocals
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.ui.components.StyledTooltip
import com.michaelflisar.composestyledcomponents.ui.components.StyledTooltipState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
class Material3Tooltip internal constructor(
    override val defaults: StyledTooltip.Defaults,
) : StyledTooltip.Implementation {

    companion object {
        @Composable
        fun create(
            defaults: StyledTooltip.Defaults = defaults(),
        ) = remember(
            defaults.shape,
            defaults.containerColor,
            defaults.contentColor,
            defaults.maxWidth,
            defaults.shadowElevation,
            defaults.contentPadding,
        ) {
            Material3Tooltip(defaults)
        }

        @Composable
        fun defaults(): StyledTooltip.Defaults {
            val shape = TooltipDefaults.plainTooltipContainerShape
            val containerColor = TooltipDefaults.plainTooltipContainerColor
            val contentColor = TooltipDefaults.plainTooltipContentColor
            val contentPadding = remember { PaddingValues(0.dp) }
            return remember(shape, containerColor, contentColor, contentPadding) {
                StyledTooltip.Defaults(
                    shape = shape,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    maxWidth = TooltipDefaults.plainTooltipMaxWidth,
                    shadowElevation = 0.dp,
                    contentPadding = contentPadding,
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        state: StyledTooltipState,
        enableUserInput: Boolean,
        focusable: Boolean,
        onDismissRequest: (() -> Unit)?,
        modifier: Modifier,
        shape: Shape,
        containerColor: Color,
        contentColor: Color,
        maxWidth: Dp,
        shadowElevation: Dp,
        contentPadding: PaddingValues,
        tooltip: @Composable () -> Unit,
        content: @Composable () -> Unit,
    ) {
        val tooltipState = rememberTooltipState(
            initialIsVisible = false,
            isPersistent = state.isPersistent,
        )
        LaunchedEffect(state, tooltipState) {
            snapshotFlow { state.isVisible }.collectLatest { isVisible ->
                if (isVisible) {
                    if (!tooltipState.isVisible) {
                        tooltipState.show()
                    }
                } else {
                    tooltipState.dismiss()
                }
            }
        }
        LaunchedEffect(state, tooltipState) {
            snapshotFlow { tooltipState.isVisible }.collect { isVisible ->
                state.updateVisibilityFromRenderer(isVisible)
            }
        }

        val positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = TooltipAnchorPosition.Above,
        )
        TooltipBox(
            positionProvider = positionProvider,
            tooltip = {
                PlainTooltip(
                    caretShape = null,
                    maxWidth = maxWidth,
                    shape = shape,
                    contentColor = contentColor,
                    containerColor = containerColor,
                    shadowElevation = shadowElevation,
                ) {
                    CopyMaterialThemeLocals(contentColor = contentColor) {
                        Box(Modifier.padding(contentPadding)) {
                            tooltip()
                        }
                    }
                }
            },
            state = tooltipState,
            modifier = modifier,
            onDismissRequest = {
                state.dismiss()
                onDismissRequest?.invoke()
            },
            focusable = focusable,
            enableUserInput = enableUserInput,
            hasAction = false,
            content = content,
        )
    }
}
