package com.michaelflisar.composestyled.demo.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.components.StyledBanner
import com.michaelflisar.composestyled.components.layout.ContentColumn
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.core.components.StyledText

@Composable
fun ScreenBanners() {
    ContentColumn(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        StyledText("Banner", style = StyledTheme.typography.titleMedium)
        StyledBanner(
            modifier = Modifier.fillMaxWidth(),
            imageVector = Icons.Default.Info,
            text = "Info Banner"
        )
        StyledBanner(
            modifier = Modifier.fillMaxWidth(),
            imageVector = Icons.Default.Warning,
            text = "Warning Banner",
            customization = StyledBanner.customize(
                background = StyledTheme.colors.error,
                content = StyledTheme.colors.onError
            )
        )
        StyledBanner(
            modifier = Modifier.fillMaxWidth(),
            imageVector = Icons.Default.Check,
            text = "Success Banner",
            customization = StyledBanner.customize(
                background = StyledTheme.colors.success,
                content = StyledTheme.colors.onSuccess
            )
        )
        StyledBanner(
            modifier = Modifier.fillMaxWidth(),
            imageVector = Icons.Default.Info,
            text = "Info Banner",
            title = "Banner with title"
        )
    }
}