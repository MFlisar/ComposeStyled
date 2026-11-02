package com.michaelflisar.composestyled.classes

import com.michaelflisar.composestyled.ui.components.StyledSideNavigation

/** Navigation presentation independent of the container and adaptive selection strategy. */
sealed interface StyledNavigationMode {

    data object Bottom : StyledNavigationMode

    data class Side(val mode: StyledSideNavigation.Mode) : StyledNavigationMode

    val isSide: Boolean
        get() = this is Side

    val isBottom: Boolean
        get() = this == Bottom
}