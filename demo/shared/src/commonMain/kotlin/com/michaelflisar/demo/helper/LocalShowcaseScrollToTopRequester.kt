package com.michaelflisar.demo.helper

import androidx.compose.runtime.staticCompositionLocalOf
import com.michaelflisar.composestyled.utils.ScrollToTopRequester

internal val LocalShowcaseScrollToTopRequester =
    staticCompositionLocalOf<ScrollToTopRequester> {
        error("The showcase scroll-to-top requester must be provided")
    }
