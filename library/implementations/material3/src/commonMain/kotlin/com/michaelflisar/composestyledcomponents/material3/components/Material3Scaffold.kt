package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.ui.components.StyledScaffold
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyledcomponents.foundation.components.FoundationScaffoldRailLayout

class Material3Scaffold internal constructor(
    override val defaults: StyledScaffold.Defaults,
) : StyledScaffold.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create(
            defaults: StyledScaffold.Defaults = defaults(),
        ) = remember(defaults) { Material3Scaffold(defaults) }

        // -------------
        // 3) defaults
        // -------------

        @Composable
        fun defaults(
            contentWindowInsets: WindowInsets = androidx.compose.material3.ScaffoldDefaults.contentWindowInsets,
        ) = StyledScaffold.Defaults(contentWindowInsets = contentWindowInsets)
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        modifier: Modifier,
        topBar: @Composable () -> Unit,
        bottomBar: @Composable () -> Unit,
        navigationRail: @Composable () -> Unit,
        snackbarHost: @Composable () -> Unit,
        floatingActionButton: @Composable () -> Unit,
        floatingActionButtonPosition: StyledScaffold.FloatingActionButtonPosition,
        containerColor: Color,
        contentColor: Color,
        contentWindowInsets: WindowInsets,
        content: @Composable (PaddingValues) -> Unit,
    ) {
        val fabPosition = when (floatingActionButtonPosition) {
            StyledScaffold.FloatingActionButtonPosition.Start -> FabPosition.Start
            StyledScaffold.FloatingActionButtonPosition.Center -> FabPosition.Center
            StyledScaffold.FloatingActionButtonPosition.End -> FabPosition.End
        }
        ProvideCustomStyledContentLocals(contentColor = contentColor) {
            Scaffold(
                modifier = modifier,
                topBar = topBar,
                bottomBar = bottomBar,
                snackbarHost = snackbarHost,
                floatingActionButton = floatingActionButton,
                floatingActionButtonPosition = fabPosition,
                containerColor = containerColor,
                contentColor = contentColor,
                contentWindowInsets = contentWindowInsets,
                content = { padding ->
                    FoundationScaffoldRailLayout(
                        padding = padding,
                        navigationRail = navigationRail,
                        content = content,
                    )
                },
            )
        }
    }
}
