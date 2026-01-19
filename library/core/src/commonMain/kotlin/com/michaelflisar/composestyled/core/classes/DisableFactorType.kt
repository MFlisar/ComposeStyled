package com.michaelflisar.composestyled.core.classes

enum class DisableFactorType {
    /**
     * used for colors that are not an outline/border nor content (text/icon/...)
     */
    Default,

    /**
     * used for outline/border colors
     */
    Outline,

    /**
     * used for content colors (text/icon/...)
     */
    Content
}