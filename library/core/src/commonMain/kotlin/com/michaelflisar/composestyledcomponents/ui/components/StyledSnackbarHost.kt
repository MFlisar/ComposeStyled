package com.michaelflisar.composestyledcomponents.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.CustomColors
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.registry.LocalStyledRegistry
import com.michaelflisar.composestyledcomponents.registry.StyledComponent
import com.michaelflisar.composestyledcomponents.registry.StyledKey
import com.michaelflisar.composestyledcomponents.ui.icons.MaterialIconsClose
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

object StyledSnackbarHost {

    // -------------
    // 1) key
    // -------------

    val Key = StyledKey<Implementation>("StyledSnackbarHost")

    // -------------
    // 5) implementation
    // -------------

    interface Implementation : StyledComponent<Implementation> {
        override val key: StyledKey<Implementation>
            get() = Key

        @Composable
        fun Render(
            hostState: StyledSnackbarHostState,
            modifier: Modifier,
            snackbarPaneTitle: String,
            snackbar: @Composable (StyledSnackbarData) -> Unit,
        )
    }
}

enum class StyledSnackbarDuration { Short, Long, Indefinite }

enum class StyledSnackbarResult { Dismissed, ActionPerformed }

@Stable
interface StyledSnackbarData {
    val message: String
    val actionLabel: String?
    val withDismissAction: Boolean
    val duration: StyledSnackbarDuration

    fun performAction()
    fun dismiss()
}

/**
 * Queues messages in order. Cancelling showSnackbar removes its queued or visible message.
 * Keep this state above theme/Scaffold switches.
 */
@Stable
class StyledSnackbarHostState {
    private val mutex = Mutex()

    var currentSnackbarData by mutableStateOf<StyledSnackbarData?>(null)
        private set

    suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: StyledSnackbarDuration = if (actionLabel == null) {
            StyledSnackbarDuration.Short
        } else {
            StyledSnackbarDuration.Indefinite
        },
    ): StyledSnackbarResult = mutex.withLock {
        try {
            suspendCancellableCoroutine { continuation ->
                currentSnackbarData = SnackbarData(
                    message, actionLabel, withDismissAction, duration, continuation,
                )
            }
        } finally {
            currentSnackbarData = null
        }
    }

    private class SnackbarData(
        override val message: String,
        override val actionLabel: String?,
        override val withDismissAction: Boolean,
        override val duration: StyledSnackbarDuration,
        private val continuation: CancellableContinuation<StyledSnackbarResult>,
    ) : StyledSnackbarData {
        override fun performAction() {
            if (continuation.isActive) continuation.resume(StyledSnackbarResult.ActionPerformed)
        }

        override fun dismiss() {
            if (continuation.isActive) continuation.resume(StyledSnackbarResult.Dismissed)
        }
    }
}

// -------------------------
// public API
// -------------------------

/**
 * Place in StyledScaffold.snackbarHost. The outgoing message remains composed during fade-out.
 * Supply localized accessibility labels, or replace snackbar for custom action rendering.
 */
@Composable
fun StyledSnackbarHost(
    hostState: StyledSnackbarHostState,
    modifier: Modifier = Modifier,
    dismissActionContentDescription: String = "Dismiss",
    snackbarPaneTitle: String = "Snackbar",
    snackbar: @Composable (StyledSnackbarData) -> Unit = { data ->
        StyledSnackbar(
            data = data,
            dismissActionContentDescription = dismissActionContentDescription,
        )
    },
) {
    LocalStyledRegistry.current[StyledSnackbarHost.Key].Render(
        hostState = hostState,
        modifier = modifier,
        snackbarPaneTitle = snackbarPaneTitle,
        snackbar = snackbar,
    )
}

@Composable
fun StyledSnackbar(
    data: StyledSnackbarData,
    modifier: Modifier = Modifier,
    actionOnNewLine: Boolean = false,
    dismissActionContentDescription: String = "Dismiss",
    shape: Shape = StyledSnackbarDefaults.shape,
    containerColor: Color = StyledTheme.colorScheme.onSurface,
    contentColor: Color = StyledTheme.contentColorFor(containerColor, checkInversely = true),
) {
    val buttonCustomization = StyledButtonDefaults.customize(
        colors = CustomColors(
            filledBackground = Color.Transparent,
            filledForeground = contentColor,
            textForeground = contentColor
        ),
        border = contentColor
    )
    val action: (@Composable () -> Unit)? = data.actionLabel?.let { label ->
        {
            StyledPrimaryButton(
                onClick = data::performAction,
                customization = buttonCustomization,
            ) {
                StyledText(label)
            }
        }
    }
    val dismissAction: (@Composable () -> Unit)? = if (data.withDismissAction) {
        {
            StyledTertiaryIconButton(
                onClick = data::dismiss,
                customization = StyledIconButtonDefaults.customize(
                    colors = CustomColors(
                        filledBackground = Color.Transparent,
                        filledForeground = contentColor,
                        textForeground = contentColor,
                    ),
                ),
            ) {
                StyledIcon(
                    icon = MaterialIconsClose.toIconComposable(),
                    contentDescription = dismissActionContentDescription,
                )
            }
        }
    } else {
        null
    }
    StyledSnackbar(
        modifier = modifier,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionOnNewLine = actionOnNewLine,
        action = action,
        dismissAction = dismissAction,
    ) {
        StyledText(data.message)
    }
}
