package com.michaelflisar.composestyled.core.classes

import androidx.compose.runtime.Immutable

@Immutable
data class StyledResolveState(
    val interaction: StyledInteractionState,
    val enabled: Boolean,
)