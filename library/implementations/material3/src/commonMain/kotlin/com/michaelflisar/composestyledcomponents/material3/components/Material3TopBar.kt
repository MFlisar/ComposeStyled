package com.michaelflisar.composestyledcomponents.material3.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyledcomponents.ProvideCustomStyledContentLocals
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.ui.components.StyledTopBar
import com.michaelflisar.composestyledcomponents.runtime.InternalComposeStyledApi

class Material3TopBar internal constructor() : StyledTopBar.Implementation {

    companion object {

        // -------------
        // 1) create
        // -------------

        @Composable
        fun create() = remember { Material3TopBar() }
    }

    @OptIn(InternalComposeStyledApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Render(
        variant: StyledTopBar.Variant,
        modifier: Modifier,
        containerColor: Color,
        contentColor: Color,
        windowInsets: WindowInsets,
        navigationIcon: @Composable () -> Unit,
        actions: @Composable RowScope.() -> Unit,
        title: @Composable () -> Unit,
    ) {
        val colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = containerColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor,
        )
        val styledTitle: @Composable () -> Unit = {
            ProvideCustomStyledContentLocals(
                contentColor = contentColor,
                textStyle = StyledTheme.typography.titleLarge,
                content = title,
            )
        }
        ProvideCustomStyledContentLocals(contentColor = contentColor) {
            when (variant) {
                StyledTopBar.Variant.Standard -> TopAppBar(
                    title = styledTitle,
                    modifier = modifier,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    windowInsets = windowInsets,
                    colors = colors,
                )
                StyledTopBar.Variant.CenterAligned -> CenterAlignedTopAppBar(
                    title = styledTitle,
                    modifier = modifier,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    windowInsets = windowInsets,
                    colors = colors,
                )
            }
        }
    }
}
