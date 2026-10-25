package com.michaelflisar.composestyledcomponents.registry

interface StyledComponent<T : Any> {
    val key: StyledKey<T>
}
