package com.michaelflisar.composestyled.core.classes

interface IVariantId {
    val id: String
}

sealed interface Variant<ID : IVariantId, Data> {

    val variantId: ID

    data class Token<ID : IVariantId, Data>(
        override val variantId: ID,
    ) : Variant<ID, Data>

    data class Custom<ID : IVariantId, Data>(
        override val variantId: ID,
        val data: Data,
    ) : Variant<ID, Data>
}

internal fun <ID : IVariantId, Data> Variant<ID, Data>.customDataOrNull(): Data? =
    (this as? Variant.Custom<ID, Data>)?.data