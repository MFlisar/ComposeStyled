package com.michaelflisar.composestyled.material3.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.material3.ProvideStyledLocals
import com.michaelflisar.composestyled.ui.layout.StyledScaffold
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.foundation.layout.FoundationScaffoldSideNavigationLayout

class Material3Scaffold internal constructor() : StyledScaffold.Implementation {

    override val defaults: StyledScaffold.Defaults
        @Composable get() = StyledScaffold.Defaults(
            contentWindowInsets = androidx.compose.material3.ScaffoldDefaults.contentWindowInsets,
        )

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3Scaffold() }
    }

    @OptIn(InternalComposeStyledApi::class)
    @Composable
    override fun Render(
        modifier: Modifier,
        topBar: @Composable () -> Unit,
        bottomBar: @Composable () -> Unit,
        sideNavigation: @Composable () -> Unit,
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
        ProvideStyledLocals(contentColor = contentColor) {
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
                    FoundationScaffoldSideNavigationLayout(
                        padding = padding,
                        sideNavigation = sideNavigation,
                        content = content,
                    )
                },
            )
        }
    }
}
