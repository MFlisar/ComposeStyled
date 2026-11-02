package com.michaelflisar.composestyled.tokens

import androidx.compose.runtime.Immutable

/** Alpha factors used to distinguish disabled containers, outlines and content. */
@Immutable
data class StyledDisableFactors(
    val default: Float,
    val outline: Float,
    val content: Float,
) {
    /** Selects which disabled alpha factor to use. */
    enum class Type {

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

    /** Returns the factor associated with [type]. */
    fun get(type: Type): Float = when (type) {
        Type.Default -> default
        Type.Outline -> outline
        Type.Content -> content
    }
}

/** Default disabled alpha factors for light color schemes. */
fun lightDisableFactors() = StyledDisableFactors(
    default = 0.38f, // Container / Surface
    outline = 0.38f, // Border / Divider
    content = 0.38f, // Text / Icon
)

/** Default disabled alpha factors for dark color schemes. */
fun darkDisableFactors() = StyledDisableFactors(
    default = 0.38f, // Container
    outline = 0.38f, // Border
    content = 0.50f, // Text / Icon stärker sichtbar
)