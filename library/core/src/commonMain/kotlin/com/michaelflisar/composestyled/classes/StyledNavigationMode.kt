package com.michaelflisar.composestyled.classes

import com.michaelflisar.composestyled.ui.components.StyledSideNavigation

/** Navigation presentation independent of the container and adaptive selection strategy. */
sealed interface StyledNavigationMode {

    /** Places primary navigation along the bottom edge. */
    data object Bottom : StyledNavigationMode

    /** Places primary navigation in a side rail or drawer using [mode]. */
    data class Side(val mode: StyledSideNavigation.Mode) : StyledNavigationMode

    /** Whether navigation is presented on the side. */
    val isSide: Boolean
        get() = this is Side

    /** Whether navigation is presented along the bottom. */
    val isBottom: Boolean
        get() = this == Bottom
}