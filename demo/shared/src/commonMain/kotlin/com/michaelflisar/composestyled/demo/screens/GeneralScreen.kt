package com.michaelflisar.composestyled.demo.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composestyled.components.layout.ContentRow
import com.michaelflisar.composestyled.core.StyledTheme
import com.michaelflisar.composestyled.demo.composables.PreviewRegionCard
import com.michaelflisar.composestyled.demo.composables.PreviewScheme
import com.michaelflisar.composestyled.demo.composables.PreviewSwatch

@Composable
fun GeneralScreen() {
    PreviewScheme(
        title = "General Theme Colors",
        modifier = Modifier.fillMaxSize()
    ) {
        Content()
    }
}

@Composable
private fun Content() {
    val c = StyledTheme.colors
    PreviewRegionCard(
        title = "Main Colors"
    ) {
        ContentRow {
            PreviewSwatch("bg", c.background, c.onBackground, Modifier.weight(1f))
            PreviewSwatch("surface", c.surface, c.onSurface, Modifier.weight(1f))
            PreviewSwatch("surf. var.", c.surfaceVariant, c.onSurfaceVariant, Modifier.weight(1f))
        }

        ContentRow {
            PreviewSwatch("primary", c.primary, c.onPrimary, Modifier.weight(1f))
            PreviewSwatch(
                "prim. surf.",
                c.primarySurface,
                c.onPrimarySurface,
                Modifier.weight(1f)
            )
            PreviewSwatch("secondary", c.secondary, c.onSecondary, Modifier.weight(1f))
            PreviewSwatch(
                "sec. surf.",
                c.secondarySurface,
                c.onSecondarySurface,
                Modifier.weight(1f)
            )
        }
    }
    PreviewRegionCard(
        title = "Status Colors"
    ) {
        ContentRow {
            PreviewSwatch("error surf.", c.errorSurface, c.onErrorSurface, Modifier.weight(1f))
            PreviewSwatch(
                "warning surf.",
                c.warningSurface,
                c.onWarningSurface,
                Modifier.weight(1f)
            )
            PreviewSwatch(
                "success surf.",
                c.successSurface,
                c.onSuccessSurface,
                Modifier.weight(1f)
            )
        }

        ContentRow {
            PreviewSwatch("error", c.error, c.onError, Modifier.weight(1f))
            PreviewSwatch("warning", c.warning, c.onWarning, Modifier.weight(1f))
            PreviewSwatch("success", c.success, c.onSuccess, Modifier.weight(1f))
        }
    }
}