package com.michaelflisar.composestyled.components.menu

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composeunstyled.DropdownPanelAnchor
import com.composeunstyled.UnstyledDropdownMenu
import com.composeunstyled.UnstyledDropdownMenuPanel
import com.michaelflisar.composestyled.core.StyledTheme

// ----------------------
// Defaults
// ----------------------

object StyledPopupMenuDefaults {

    val containerShape: Shape
        @Composable @ReadOnlyComposable get() = StyledTheme.shapes.container

    val itemShape: Shape
        @Composable @ReadOnlyComposable get() = StyledTheme.shapes.control

    val contentPadding: PaddingValues
        @Composable @ReadOnlyComposable get() = PaddingValues(StyledTheme.paddings.small)

    val containerElevation: Dp
        @Composable @ReadOnlyComposable get() = StyledTheme.elevations.medium

    val enterAnimation = scaleIn(
        animationSpec = tween(durationMillis = 120, easing = LinearOutSlowInEasing),
        initialScale = 0.8f,
        transformOrigin = TransformOrigin(0f, 0f)
    ) + fadeIn(tween(durationMillis = 30))

    val exitAnimation = scaleOut(
        animationSpec = tween(durationMillis = 1, delayMillis = 75),
        targetScale = 1f
    )+ fadeOut(tween(durationMillis = 75))
}

@Composable
fun PopupMenuBox(
    content: @Composable () -> Unit,
    menu: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.BottomStart
    ) {
        content()
        menu()
    }
}

@Composable
fun PopupMenu(
    state: MenuState,
    modifier: Modifier = Modifier,
    setup: MenuSetup = rememberMenuSetup(),
    content: @Composable MenuScope.() -> Unit,
) {
    //if (!state.visible)
    //return

    ProvideMenuLocals(
        state = state,
        setup = setup,
        level = -1 // we still outside of the menu
    ) {
        UnstyledDropdownMenu(
            onExpandRequest = { state.show() }
        ) {
            val density = LocalDensity.current
            UnstyledDropdownMenuPanel(
                expanded = state.visible,
                anchor = DropdownPanelAnchor.BottomStart,
                onDismissRequest = { state.hide() },
                backgroundColor = StyledTheme.colors.surfaceVariant,
                contentColor = StyledTheme.colors.onSurfaceVariant,
                contentPadding = StyledPopupMenuDefaults.contentPadding,
                shape = StyledPopupMenuDefaults.containerShape,
                modifier = Modifier
                    .offset(
                        x = setup.offset.x,
                        y = setup.offset.y
                    )
                    .offset(
                        x = with(density) { state.offset.value.x.toDp() },
                        y = with(density) { state.offset.value.y.toDp() }
                    )
                    .width(240.dp)
                    .shadow(
                        StyledPopupMenuDefaults.containerElevation,
                        StyledPopupMenuDefaults.containerShape,
                    ),
                enter = StyledPopupMenuDefaults.enterAnimation,
                exit = StyledPopupMenuDefaults.exitAnimation
            ) {
                ProvideUpdatedMenuLocals(emptyList(), 0) {
                    with(remember { MenuScopeInstance() }) {
                        content()
                    }
                }
            }
        }
        /*
        DropdownMenu(
            modifier = modifier
                .onGloballyPositioned {
                    state.offset.value = it.positionOnScreen().round()
                }
            // springt damit falls sich die Position des s durch die sich ändernde Größe verändert!
            //.animateContentSize()
            ,
            expanded = true,
            onDismissRequest = { state.hide() },
            offset = setup.offset,
            shape = RoundedCornerShape(StyledTheme.shapes.small)
        ) {
            ProvideUpdatedMenuLocals(emptyList(), 0) {
                with(remember { MenuScopeInstance() }) {
                    content()
                }
            }
        }*/
    }
}