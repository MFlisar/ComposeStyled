package com.michaelflisar.demo.helper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composestyled.StyledTheme
import com.michaelflisar.composestyled.classes.toIconComposable
import com.michaelflisar.composestyled.ui.components.StyledBottomNavigationBar
import com.michaelflisar.composestyled.ui.components.StyledExpandableTopBar
import com.michaelflisar.composestyled.ui.components.StyledIcon
import com.michaelflisar.composestyled.ui.components.StyledNavigationItem
import com.michaelflisar.composestyled.ui.components.StyledSideNavigation
import com.michaelflisar.composestyled.ui.components.StyledIconButton
import com.michaelflisar.composestyled.ui.components.StyledTopBar
import com.michaelflisar.composestyled.ui.primitives.StyledText
import com.michaelflisar.composestyled.ui.state.StyledExpandableTopBarScrollBehavior

@Composable
internal fun DemoTopBar(
    containerColor: Color,
    title: String = "Styled Components",
    onBack: (() -> Unit)? = null,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
    scrollBehavior: StyledExpandableTopBarScrollBehavior? = null,
) {
    val navigationIcon: @Composable () -> Unit = {
        if (onBack != null) {
            StyledIconButton(onClick = onBack) {
                StyledIcon(
                    icon = Icons.AutoMirrored.Filled.ArrowBack.toIconComposable(),
                    contentDescription = "Back to demo selector",
                )
            }
        }
    }
    val titleContent: @Composable () -> Unit = { StyledText(title) }
    if (scrollBehavior != null) {
        StyledExpandableTopBar(
            behavior = scrollBehavior,
            navigationIcon = navigationIcon,
            title = titleContent,
            containerColor = containerColor,
            contentColor = contentColor,
        )
    } else {
        StyledTopBar(
            navigationIcon = navigationIcon,
            title = titleContent,
            containerColor = containerColor,
            contentColor = contentColor,
        )
    }
}

@Composable
internal fun DemoBottomNavigation(
    navigationItems: List<StyledNavigationItem<Int>>,
    selectedDestination: MutableState<Int>,
    containerColor: Color,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
) {
    StyledBottomNavigationBar(
        items = navigationItems,
        selectedKey = selectedDestination.value,
        containerColor = containerColor,
        contentColor = contentColor,
        onItemClick = { selectedDestination.value = it.key }
    )
}

@Composable
internal fun DemoSideNavigation(
    navigationItems: List<StyledNavigationItem<Int>>,
    selectedDestination: MutableState<Int>,
    mode: StyledSideNavigation.Mode,
    expanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    containerColor: Color = StyledTheme.colorScheme.surfaceVariant,
    contentColor: Color = StyledTheme.contentColorFor(containerColor),
) {
    StyledSideNavigation(
        items = navigationItems,
        selectedKey = selectedDestination.value,
        onItemClick = { selectedDestination.value = it.key },
        mode = mode,
        expanded = expanded,
        containerColor = containerColor,
        contentColor = contentColor,
        title = { StyledText("Components") },
    )
}
