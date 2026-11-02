package com.michaelflisar.composestyled.ui.icons

import androidx.compose.runtime.Stable
import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey

/** Resolves semantic [StyledIcons.Name] values to icons supplied by a theme or icon-set module. */
@Stable
interface StyledIconSet : StyledComponent<StyledIconSet> {

    companion object {
        val Key = StyledKey<StyledIconSet>("StyledIconSet")
    }

    override val key: StyledKey<StyledIconSet>
        get() = Key

    /** Returns the icon implementation for [icon]. */
    fun resolve(icon: StyledIcons.Name): IconComposable
}
