package com.michaelflisar.composestyled.ui.icons

import com.michaelflisar.composestyled.classes.IconComposable
import com.michaelflisar.composestyled.registry.StyledComponent
import com.michaelflisar.composestyled.registry.StyledKey

interface StyledIconSet : StyledComponent<StyledIconSet> {

    companion object {
        val Key = StyledKey<StyledIconSet>("StyledIconSet")
    }

    override val key: StyledKey<StyledIconSet>
        get() = Key

    fun resolve(icon: StyledIcons.Name): IconComposable
}
