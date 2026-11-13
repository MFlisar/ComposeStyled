package com.michaelflisar.composechangelog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue

@Stable
class ChangelogState internal constructor(
    visible: Boolean,
    minimumVisibleReleaseVersion: Int?,
) {
    var visible by mutableStateOf(visible)
        private set

    var minimumVisibleReleaseVersion by mutableStateOf(minimumVisibleReleaseVersion)
        private set

    /** Shows all configured releases. */
    fun show() {
        minimumVisibleReleaseVersion = null
        visible = true
    }

    /** Shows releases whose version code is at least [minimumVersion]. */
    fun show(minimumVersion: Int) {
        require(minimumVersion >= 0) { "Minimum version code must not be negative." }
        minimumVisibleReleaseVersion = minimumVersion
        visible = true
    }

    /** Hides the changelog without changing its minimum version filter. */
    fun hide() {
        visible = false
    }
}

/**
 * Remembers a manually controlled changelog state.
 *
 * [visible] and [minimumVisibleReleaseVersion] are the initial state values.
 */
@Composable
fun rememberChangelogState(
    visible: Boolean = false,
    minimumVisibleReleaseVersion: Int? = null,
): ChangelogState {
    require(minimumVisibleReleaseVersion == null || minimumVisibleReleaseVersion >= 0) {
        "Minimum version code must not be negative."
    }
    return remember(visible, minimumVisibleReleaseVersion) {
        ChangelogState(visible, minimumVisibleReleaseVersion)
    }
}

/**
 * Opens the changelog for releases newer than [lastShownVersion] after an app update.
 *
 * On first launch this stores the current app version without opening the changelog.
 * The callback is invoked from a composition coroutine.
 */
@Composable
fun rememberChangelogState(
    setup: ChangelogSetup,
    lastShownVersion: Int?,
    saveLastShownVersion: suspend (Int) -> Unit,
): ChangelogState {
    require(lastShownVersion == null || lastShownVersion >= 0) {
        "Last shown version code must not be negative."
    }

    val state = remember { ChangelogState(visible = false, minimumVisibleReleaseVersion = null) }
    val saveVersion = rememberUpdatedState(saveLastShownVersion)

    LaunchedEffect(setup.appVersion.code, lastShownVersion) {
        when {
            lastShownVersion == null -> saveVersion.value(setup.appVersion.code)
            setup.appVersion.code > lastShownVersion -> {
                state.show(lastShownVersion + 1)
                saveVersion.value(setup.appVersion.code)
            }
        }
    }

    return state
}
