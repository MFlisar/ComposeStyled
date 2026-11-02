package com.michaelflisar.composestyled.ui.icons.sfsymbols

import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.ui.icons.StyledIcons
import com.michaelflisar.composestyled.ui.icons.material3.Material3FilledIconSet

internal actual fun platformIcon(
    icon: StyledIcons.Name,
    @Suppress("UNUSED_PARAMETER") symbol: String,
): IconComposable =
    Material3FilledIconSet.resolve(icon)
