package com.michaelflisar.composestyled.core.classes

import androidx.compose.runtime.Composable
import com.composeunstyled.theme.Theme
import com.composeunstyled.theme.ThemeProperty
import com.composeunstyled.theme.ThemeToken
import com.michaelflisar.composestyled.core.runtime.LocalThemeBuilder

internal class TokenMap<ID, Data>(
    private val property: ThemeProperty<Data>,
    private val map: Map<ID, ThemeToken<Data>>,
    private val allIds: Set<ID>,
) where ID : Enum<ID>, ID : IVariantId {

    companion object {
        inline fun <reified ID, Data> create(property: ThemeProperty<Data>): TokenMap<ID, Data> where ID : Enum<ID>, ID : IVariantId {
            val keys = enumValues<ID>()
            val map = keys.associateWith { id -> ThemeToken<Data>(id.id) }
            return TokenMap(property, map, keys.toSet())
        }
    }

    fun tokenOf(id: ID) = map.getValue(id)

    @Composable
    fun resolveVariantData(
        variant: Variant<ID, Data>,
    ): Data {
        return when (variant) {
            is Variant.Token -> Theme[property][tokenOf(variant.variantId)]
            is Variant.Custom -> variant.data
        }
    }

    @Composable
    fun registerStyles(
        styles: Map<ID, Data>,
    ) {
        require(styles.keys == allIds) { "Missing styles for: ${allIds - styles.keys}" }
        with(LocalThemeBuilder.current) {
            properties[property] = styles.entries.associate { (id, value) -> tokenOf(id) to value }
            properties[property] = styles.mapKeys { tokenOf(it.key) }
        }
    }
}