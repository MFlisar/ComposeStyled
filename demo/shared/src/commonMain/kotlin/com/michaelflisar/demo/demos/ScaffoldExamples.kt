package com.michaelflisar.demo.demos

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.michaelflisar.composestyledcomponents.StyledTheme
import com.michaelflisar.composestyledcomponents.classes.toIconComposable
import com.michaelflisar.composestyledcomponents.ui.layout.StyledSpacedFlowRow
import com.michaelflisar.composestyledcomponents.ui.primitives.StyledText
import com.michaelflisar.composestyledcomponents.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyledcomponents.ui.components.StyledFloatingActionButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledSmallFloatingActionButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledLargeFloatingActionButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledIcon
import com.michaelflisar.composestyledcomponents.ui.components.StyledNavigationItem
import com.michaelflisar.composestyledcomponents.ui.components.StyledNavigationRail
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbar
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarData
import com.michaelflisar.composestyledcomponents.ui.components.StyledSnackbarDuration
import com.michaelflisar.composestyledcomponents.ui.components.StyledScaffold
import com.michaelflisar.composestyledcomponents.ui.components.StyledTopBar
import com.michaelflisar.composestyledcomponents.ui.components.StyledCenterAlignedTopBar
import com.michaelflisar.composestyledcomponents.ui.components.StyledTertiaryButton
import com.michaelflisar.composestyledcomponents.ui.components.StyledPrimaryButton
import com.michaelflisar.demo.helper.RegionContainer
import com.michaelflisar.demo.helper.SubTitle
import com.michaelflisar.demo.helper.ExpandState

@Composable
internal fun ScaffoldExamples(
    baseId: Int,
    expandState: ExpandState,
    showSnackbar: () -> Unit
) {
    RegionContainer(
        title = "Scaffold",
        id = baseId,
        expanded = expandState,
    ) {
        SubTitle("Top bar, content and floating action button")
        StyledScaffold(
            modifier = Modifier.fillMaxWidth().height(240.dp),
            topBar = {
                StyledTopBar(
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    title = { StyledText("Scaffold preview") },
                )
            },
            floatingActionButton = {
                StyledSmallFloatingActionButton(
                    onClick = showSnackbar,
                ) {
                    StyledIcon(
                        icon = Icons.Default.Add.toIconComposable(),
                        contentDescription = "Show snackbar",
                    )
                }
            },
            content = { paddingValues ->
                StyledText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    text = "Scaffold applies content padding around its slots.",
                )
            },
        )
    }
    RegionContainer(
        title = "Top bars",
        id = baseId + 1,
        expanded = expandState
    ) {
        SubTitle("Default Top Bars (primary surface")
        StyledTopBar.Variant.entries.forEach { variant ->
            val title: @Composable () -> Unit = { StyledText(variant.name) }
            if (variant == StyledTopBar.Variant.Standard) {
                StyledTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    title = title,
                    containerColor = StyledTheme.colorScheme.primarySurface,
                    contentColor = StyledTheme.colorScheme.onPrimarySurface,
                )
            } else {
                StyledCenterAlignedTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    title = title,
                    containerColor = StyledTheme.colorScheme.primarySurface,
                    contentColor = StyledTheme.colorScheme.onPrimarySurface,
                )
            }
        }
        SubTitle("Top Bars with Customization")
        StyledTopBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = StyledTheme.colorScheme.primarySurface,
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                StyledTertiaryButton(onClick = showSnackbar) {
                    StyledIcon(icon = Icons.AutoMirrored.Filled.ArrowBack.toIconComposable(), contentDescription = "Back")
                }
            },
            actions = {
                StyledTertiaryButton(onClick = showSnackbar) {
                    StyledIcon(icon = Icons.Default.Add.toIconComposable(), contentDescription = "Add")
                }
            },
            title = { StyledText("Navigation and actions") },
        )
    }
    RegionContainer(
        title = "Navigation",
        id = baseId + 2,
        expanded = expandState
    ) {
        val selected = remember { mutableStateOf(0) }
        val items = remember {
            listOf(
                StyledNavigationItem(
                    key = 0,
                    label = { StyledText("Home") },
                    icon = { StyledIcon(icon = Icons.Default.Check.toIconComposable(), contentDescription = null) },
                ),
                StyledNavigationItem(
                    key = 1,
                    label = { StyledText("More") },
                    icon = { StyledIcon(icon = Icons.Default.Add.toIconComposable(), contentDescription = null) },
                ),
                StyledNavigationItem(
                    key = 2,
                    label = { StyledText("Disabled") },
                    icon = { StyledIcon(icon = Icons.Default.Add.toIconComposable(), contentDescription = null) },
                    enabled = false,
                ),
            )
        }
        SubTitle("Bottom Navigation")
        StyledBottomNavigationBar(
            items = items,
            selectedKey = selected.value,
            onItemClick = { selected.value = it.key },
            windowInsets = WindowInsets(0, 0, 0, 0),
        )
        SubTitle("Navigation Rail")
        StyledNavigationRail(
            items = items,
            selectedKey = selected.value,
            onItemClick = { selected.value = it.key },
            modifier = Modifier.height(280.dp),
        )
    }
    RegionContainer(
        title = "Floating action buttons",
        id = baseId + 3,
        expanded = expandState
    ) {
        SubTitle("Default Floating Action Buttons")
        StyledSpacedFlowRow {
            StyledFloatingActionButton.Variant.entries.forEach { variant ->
                val content: @Composable () -> Unit = {
                    StyledIcon(icon = Icons.Default.Add.toIconComposable(), contentDescription = variant.name)
                }
                when (variant) {
                    StyledFloatingActionButton.Variant.Small -> StyledSmallFloatingActionButton(onClick = showSnackbar, content = content)
                    StyledFloatingActionButton.Variant.Standard -> StyledFloatingActionButton(onClick = showSnackbar, content = content)
                    StyledFloatingActionButton.Variant.Large -> StyledLargeFloatingActionButton(onClick = showSnackbar, content = content)
                }
            }
        }
        SubTitle("Floating Action Buttons with Customization")
        StyledSpacedFlowRow {
            StyledFloatingActionButton(
                onClick = showSnackbar,
                containerColor = Color.Magenta,
                contentColor = Color.White,
                shape = StyledTheme.shapes.small,
            ) {
                StyledIcon(icon = Icons.Default.Add.toIconComposable(), contentDescription = "Custom action")
            }
        }
    }
    RegionContainer(
        title = "Snackbars",
        id = baseId + 4,
        expanded = expandState
    ) {
        SubTitle("Default Snackbar")
        StyledSnackbar { StyledText("Changes saved") }
        SubTitle("Same message with dismiss icon")
        val dismissibleData = remember(showSnackbar) {
            object : StyledSnackbarData {
                override val message = "Changes saved"
                override val actionLabel: String? = null
                override val withDismissAction = true
                override val duration = StyledSnackbarDuration.Indefinite
                override fun performAction() = showSnackbar()
                override fun dismiss() = showSnackbar()
            }
        }
        StyledSnackbar(data = dismissibleData)
        SubTitle("Snackbar with Customization")
        StyledSnackbar(
            containerColor = StyledTheme.colorScheme.primarySurface,
            contentColor = StyledTheme.colorScheme.onPrimarySurface,
            shape = StyledTheme.shapes.large,
            action = {
                StyledTertiaryButton(onClick = showSnackbar) {
                    StyledText("Undo")
                }
            },
        ) {
            StyledText("Changes saved with action")
        }
        SubTitle("Scaffold Snackbar Host")
        StyledPrimaryButton(onClick = showSnackbar) {
            StyledText("Show snackbar with action")
        }
    }
}
