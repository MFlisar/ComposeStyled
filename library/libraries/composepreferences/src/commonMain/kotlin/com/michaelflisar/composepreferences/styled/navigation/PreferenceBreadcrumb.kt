package com.michaelflisar.composepreferences.styled.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composepreferences.styled.LocalStyledPreferenceStrings
import com.michaelflisar.composepreferences.styled.PreferenceFilter
import com.michaelflisar.composepreferences.styled.layout.PreferenceScreenState
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.specials.StyledBreadcrumb
import com.michaelflisar.composestyled.ui.specials.StyledBreadcrumbItem
import com.michaelflisar.composestyled.ui.specials.StyledBreadcrumbStyle
import com.michaelflisar.composestyled.ui.specials.rememberStyledBreadcrumbStyle

/** Displays the preference path or active filter scope and lets users navigate to earlier groups. */
@Composable
fun PreferenceBreadcrumb(
    state: PreferenceScreenState,
    modifier: Modifier = Modifier,
    rootLabel: String? = null,
    style: StyledBreadcrumbStyle = rememberStyledBreadcrumbStyle(),
    separatorIcon: IconComposable = StyledIcons.Submenu,
    filter: PreferenceFilter? = null,
) {
    val strings = LocalStyledPreferenceStrings.current
    val routes = state.routes
    val activeFilter = filter?.takeIf { it.query.isNotBlank() }
    val isGlobalFilter = activeFilter?.scope == PreferenceFilter.Scope.Global
    val items = buildList {
        add(
            StyledBreadcrumbItem(
                label = rootLabel ?: strings.preferences,
                onClick = if (routes.isNotEmpty() && !isGlobalFilter) {
                    { state.navigateToDepth(0) }
                } else {
                    null
                },
            )
        )
        if (!isGlobalFilter) {
            routes.forEachIndexed { index, route ->
                add(
                    StyledBreadcrumbItem(
                        label = route.title,
                        onClick = if (index < routes.lastIndex) {
                            { state.navigateToDepth(index + 1) }
                        } else {
                            null
                        },
                    )
                )
            }
        }
        activeFilter?.let {
            add(
                StyledBreadcrumbItem(
                    label = if (isGlobalFilter) {
                        strings.searchingEverywhere
                    } else {
                        strings.filtered
                    },
                    onClick = null,
                )
            )
        }
    }
    StyledBreadcrumb(
        items = items,
        modifier = modifier,
        style = style,
        separatorIcon = separatorIcon,
    )
}
