package com.michaelflisar.composestyled.ui.components

/** Controls whether clicking a toggleable row changes its value or only the control does. */
enum class StyledToggleBehavior {
    /** Only the checkbox, radio button or switch itself handles clicks. */
    ControlOnly,

    /** Clicking anywhere on the item toggles the control. */
    FullItem,
}
