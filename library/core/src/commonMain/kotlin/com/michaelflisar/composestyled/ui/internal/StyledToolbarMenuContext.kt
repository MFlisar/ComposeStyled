package com.michaelflisar.composestyled.ui.internal

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal val LocalStyledToolbarMenuLevel =
    staticCompositionLocalOf<StyledToolbarMenuLevel?> { null }

internal val LocalStyledToolbarPopupContext =
    staticCompositionLocalOf<StyledToolbarPopupContext?> { null }

internal val LocalStyledToolbarSubmenuAnchor = staticCompositionLocalOf { false }

@Stable
internal class StyledToolbarMenuLevel(
    private val coroutineScope: CoroutineScope,
) {
    var activeId by mutableStateOf<Any?>(null)
        private set

    private var pinnedId: Any? = null
    private var popupDismissedByAnchorId: Any? = null
    private val completedPopupAnimations = mutableSetOf<Any>()
    private val hoverTargets = mutableMapOf<Any, MutableSet<Any>>()
    private val openJobs = mutableMapOf<Any, Job>()
    private val closeJobs = mutableMapOf<Any, Job>()
    private val childLevels = mutableMapOf<Any, StyledToolbarMenuLevel>()

    fun isActive(id: Any): Boolean = activeId == id

    fun createChildLevel(id: Any) =
        childLevels.getOrPut(id) { StyledToolbarMenuLevel(coroutineScope) }

    fun openFromHover(id: Any) {
        activate(id, pinned = false)
    }

    fun openFromClick(id: Any) {
        if (popupDismissedByAnchorId == id) {
            popupDismissedByAnchorId = null
            return
        }
        if (activeId == id) {
            closeJobs.remove(id)?.cancel()
            if (id in completedPopupAnimations) {
                close(id)
            }
            return
        }
        if (openJobs.containsKey(id)) {
            return
        }
        activate(id, pinned = true)
    }

    fun closeFromPopup(id: Any, anchorHovered: Boolean) {
        if (activeId == id && anchorHovered) {
            if (id in completedPopupAnimations) {
                close(id)
                popupDismissedByAnchorId = id
            }
        } else {
            close(id)
        }
    }

    fun setPopupAnimationFinished(id: Any, finished: Boolean) {
        if (finished && activeId == id) {
            completedPopupAnimations.add(id)
        } else {
            completedPopupAnimations.remove(id)
        }
    }

    fun close(id: Any) {
        openJobs.remove(id)?.cancel()
        closeJobs.remove(id)?.cancel()
        hoverTargets.remove(id)
        childLevels[id]?.reset()
        completedPopupAnimations.remove(id)
        if (popupDismissedByAnchorId == id) {
            popupDismissedByAnchorId = null
        }
        if (activeId == id) {
            activeId = null
            pinnedId = null
        }
    }

    fun closeActive() {
        activeId?.let(::close)
    }

    fun setHoverTarget(
        id: Any,
        target: Any,
        hovered: Boolean,
        openOnHover: Boolean = false,
    ) {
        if (hovered) {
            if (popupDismissedByAnchorId == id) {
                popupDismissedByAnchorId = null
            }
            closeJobs.remove(id)?.cancel()
            hoverTargets.getOrPut(id) { mutableSetOf() }.add(target)
            if (openOnHover) {
                when {
                    activeId == null -> scheduleOpen(id)
                    activeId != id -> openFromHover(id)
                    else -> openJobs.remove(id)?.cancel()
                }
            }
        } else {
            openJobs.remove(id)?.cancel()
            hoverTargets[id]?.let { targets ->
                targets.remove(target)
                if (targets.isEmpty()) {
                    hoverTargets.remove(id)
                }
            }
            // An anchor can lose hover when its popup is created without leaving the menu tree.
            if (!openOnHover) {
                scheduleClose(id)
            }
        }
    }

    private fun activate(id: Any, pinned: Boolean) {
        popupDismissedByAnchorId = null
        if (activeId != id) {
            activeId?.let { previousId ->
                openJobs.remove(previousId)?.cancel()
                closeJobs.remove(previousId)?.cancel()
                hoverTargets.remove(previousId)
                completedPopupAnimations.remove(previousId)
                childLevels[previousId]?.reset()
            }
        }
        openJobs.remove(id)?.cancel()
        closeJobs.remove(id)?.cancel()
        activeId = id
        pinnedId = if (pinned) id else null
    }

    private fun reset() {
        openJobs.values.forEach { it.cancel() }
        openJobs.clear()
        closeJobs.values.forEach { it.cancel() }
        closeJobs.clear()
        hoverTargets.clear()
        completedPopupAnimations.clear()
        childLevels.values.forEach { it.reset() }
        popupDismissedByAnchorId = null
        activeId = null
        pinnedId = null
    }

    private fun scheduleOpen(id: Any) {
        openJobs.remove(id)?.cancel()
        openJobs[id] = coroutineScope.launch {
            delay(OpenDelayMillis)
            if (activeId == null && !hoverTargets[id].isNullOrEmpty()) {
                openFromHover(id)
            }
        }
    }

    private fun scheduleClose(id: Any) {
        closeJobs.remove(id)?.cancel()
        if (pinnedId == id) return
        closeJobs[id] = coroutineScope.launch {
            delay(CloseDelayMillis)
            if (
                activeId == id &&
                pinnedId != id &&
                hoverTargets[id].isNullOrEmpty()
            ) {
                close(id)
            }
        }
    }

    private companion object {
        const val OpenDelayMillis = 180L
        const val CloseDelayMillis = 260L
    }
}

@Stable
internal class StyledToolbarMenuAncestor(
    val level: StyledToolbarMenuLevel,
    val id: Any,
)

@Stable
internal class StyledToolbarPopupContext(
    val rootLevel: StyledToolbarMenuLevel,
    val rootId: Any,
    val menuLevel: StyledToolbarMenuLevel,
    val ancestors: List<StyledToolbarMenuAncestor> = emptyList(),
)

@Composable
internal fun ToolbarPopupMenuContent(
    context: StyledToolbarPopupContext,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interactionSource = remember(context) { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val hoverTarget = remember(context) { Any() }
    var hasBeenHovered by remember(context) { mutableStateOf(false) }

    LaunchedEffect(context, hovered) {
        if (hovered) {
            hasBeenHovered = true
            context.setHoverTarget(hoverTarget, true)
        } else if (hasBeenHovered) {
            context.setHoverTarget(hoverTarget, false)
        }
    }

    CompositionLocalProvider(LocalStyledToolbarPopupContext provides context) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .hoverable(interactionSource),
        ) {
            Column(content = content)
        }
    }
}

private fun StyledToolbarPopupContext.setHoverTarget(
    target: Any,
    hovered: Boolean,
) {
    rootLevel.setHoverTarget(rootId, target, hovered)
    ancestors.forEach { ancestor ->
        ancestor.level.setHoverTarget(ancestor.id, target, hovered)
    }
}
