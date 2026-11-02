package com.michaelflisar.composestyled.adaptive

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.michaelflisar.composestyled.classes.StyledNavigationMode
import com.michaelflisar.composestyled.ui.layout.StyledDialog

/**
 * Read-only adaptive information reported by one StyledAdaptiveScaffold.
 * Create one instance per scaffold with `remember { StyledAdaptiveScaffoldState() }`.
 */
@Stable
class StyledAdaptiveScaffoldState {
    /** Last committed decisions for this scaffold, or null before its first layout. */
    var info: StyledAdaptiveInfo? by mutableStateOf(null)
        private set

    /** The last committed navigation mode selected from the scaffold's constraints. */
    val navigationMode: StyledNavigationMode?
        get() = info?.navigationMode

    /** The last committed master-detail mode for the scaffold's overall constraints. */
    val masterDetailMode: StyledAdaptiveMasterDetailMode?
        get() = info?.masterDetailMode

    /** The last committed dialog style selected from the host window size. */
    val dialogStyle: StyledDialog.Style?
        get() = info?.dialogStyle

    internal fun update(info: StyledAdaptiveInfo) {
        this.info = info
    }
}
