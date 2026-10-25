package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarData
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarDuration
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarHost
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarHostState

class Material3SnackbarHost internal constructor() : StyledSnackbarHost.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3SnackbarHost() }
    }

    @Composable
    override fun Render(
        hostState: StyledSnackbarHostState,
        modifier: Modifier,
        snackbarPaneTitle: String,
        snackbar: @Composable (StyledSnackbarData) -> Unit,
    ) {
        val nativeHostState = remember(hostState) { SnackbarHostState() }
        val data = hostState.currentSnackbarData
        LaunchedEffect(nativeHostState, data) {
            if (data != null) {
                val result = nativeHostState.showSnackbar(
                    visuals = Material3SnackbarVisuals(data),
                )
                when (result) {
                    SnackbarResult.ActionPerformed -> data.performAction()
                    SnackbarResult.Dismissed -> data.dismiss()
                }
            }
        }
        SnackbarHost(
            hostState = nativeHostState,
            modifier = modifier,
        ) { nativeData ->
            val visuals = nativeData.visuals as Material3SnackbarVisuals
            Box(
                Modifier.padding(StyledTheme.padding.content).semantics {
                    paneTitle = snackbarPaneTitle
                }
            ) {
                snackbar(visuals.data)
            }
        }
    }
}

private class Material3SnackbarVisuals(val data: StyledSnackbarData) : SnackbarVisuals {
    override val message: String get() = data.message
    override val actionLabel: String? get() = data.actionLabel
    override val withDismissAction: Boolean get() = data.withDismissAction
    override val duration: SnackbarDuration
        get() = when (data.duration) {
            StyledSnackbarDuration.Short -> SnackbarDuration.Short
            StyledSnackbarDuration.Long -> SnackbarDuration.Long
            StyledSnackbarDuration.Indefinite -> SnackbarDuration.Indefinite
        }
}
