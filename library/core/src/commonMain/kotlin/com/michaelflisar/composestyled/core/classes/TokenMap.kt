package com.michaelflisar.composestyled.core.classes

import androidx.compose.runtime.Composable
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.runtime.LocalThemeBuilder

internal class TokenMap<V : IVariant, Data>(
    private val property: ThemeProperty<Data>,
    private val map: Map<V, ThemeToken<Data>>,
    private val variants: Set<V>,
) {

    companion object {
        fun <V : IVariant, Data> create(
            property: ThemeProperty<Data>,
            variants: Set<V>,
        ): TokenMap<V, Data> {
            val map = variants.associateWith { id -> ThemeToken<Data>(id.id) }
            return TokenMap(property, map, variants)
        }
    }

    private fun tokenOf(id: V) = map.getValue(id)

    @Composable
    fun resolveVariantData(
        variant: V,
    ): Data {
        return Theme[property][tokenOf(variant)]
    }

    @Composable
    fun registerStyles(
        styles: Map<V, Data>,
    ) {
        require(styles.keys == variants) { "Missing styles for: ${variants - styles.keys}" }
        with(LocalThemeBuilder.current) {
            properties[property] = styles.entries.associate { (id, value) -> tokenOf(id) to value }
        }
    }
}