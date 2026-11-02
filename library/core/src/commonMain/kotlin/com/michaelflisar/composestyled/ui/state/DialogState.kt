package com.michaelflisar.composestyled.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/** Shared visibility contract for every Styled dialog and sheet. */
@Stable
interface DialogState {
    val visible: Boolean
    fun dismiss()
}

@Stable
class VisibilityDialogState internal constructor(initialVisible: Boolean) : DialogState {
    override var visible by mutableStateOf(initialVisible)
        private set

    fun show() {
        visible = true
    }

    override fun dismiss() {
        visible = false
    }
}

/** Non-null data is the single source of truth for visibility. */
@Stable
class DataDialogState<T : Any> internal constructor(initialData: T?) : DialogState {
    var data by mutableStateOf(initialData)
        private set
    private var dismissedData: T? = null
    private var dismissalAnimationEnabled = false

    override val visible: Boolean
        get() = data != null

    fun show(data: T) {
        dismissedData = null
        this.data = data
    }

    override fun dismiss() {
        dismissedData = if (dismissalAnimationEnabled) data else null
        data = null
    }

    /** Retains the dismissed value while an animated Styled dialog completes its exit. */
    fun requireData(): T = checkNotNull(data ?: dismissedData) {
        "The dialog has no data; show it before requiring data."
    }

    internal fun finishDismissalAnimation() {
        dismissedData = null
    }

    internal fun setDismissalAnimationEnabled(enabled: Boolean) {
        dismissalAnimationEnabled = enabled
    }
}

/** Creates state outside composition, for example as a ViewModel property. */
fun dialogStateOf(initialVisible: Boolean = false): VisibilityDialogState =
    VisibilityDialogState(initialVisible)

fun <T : Any> dialogStateOf(initialData: T?): DataDialogState<T> = DataDialogState(initialData)

@Composable
fun rememberDialogState(initialVisible: Boolean = false): VisibilityDialogState =
    rememberSaveable(saver = VisibilityDialogStateSaver) { dialogStateOf(initialVisible) }

/** [initialData] is used only when this composition first creates the state. */
@Composable
fun <T : Any> rememberDialogState(initialData: T?): DataDialogState<T> =
    remember { dialogStateOf<T>(initialData) }

/**
 * Creates a saveable data dialog state using [dataSaver] to persist its current data.
 * The saver is required because arbitrary [T] values are not necessarily saveable.
 */
@Composable
fun <T : Any, S : Any> rememberDialogState(
    initialData: T?,
    dataSaver: Saver<T, S>,
): DataDialogState<T> {
    val stateSaver = remember(dataSaver) {
        Saver<DataDialogState<T>, List<Any>>(
            save = { state ->
                val data = state.data
                if (data == null) {
                    listOf(false)
                } else {
                    listOf(
                        true,
                        checkNotNull(with(dataSaver) { save(data) }) {
                            "The dialog data saver returned null for a non-null value."
                        },
                    )
                }
            },
            restore = { saved ->
                val data = if (saved.first() as Boolean) {
                    @Suppress("UNCHECKED_CAST")
                    checkNotNull(with(dataSaver) { restore(saved[1] as S) }) {
                            "The dialog data saver could not restore its saved value."
                        }
                } else {
                    null
                }
                DataDialogState(data)
            },
        )
    }
    return rememberSaveable(saver = stateSaver) { dialogStateOf(initialData) }
}

private val VisibilityDialogStateSaver: Saver<VisibilityDialogState, Boolean> = Saver(
    save = { it.visible },
    restore = { VisibilityDialogState(it) },
)
