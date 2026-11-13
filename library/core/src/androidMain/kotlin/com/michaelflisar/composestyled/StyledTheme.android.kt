package com.michaelflisar.composestyled

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.michaelflisar.composestyled.runtime.InternalComposeStyledApi
import com.michaelflisar.composestyled.themes.StyledThemeColors
import com.michaelflisar.composestyled.themes.createStyledThemeColors

actual val StyledTheme.isDynamicColorsSupported: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

actual val StyledTheme.isSystemContrastSupported: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE

@Composable
@OptIn(InternalComposeStyledApi::class)
internal actual fun platformDynamicThemeColors(): StyledThemeColors? {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    return remember(context, configuration) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            null
        } else {
            createStyledThemeColors(
                themeId = "android",
                id = "dynamic",
                name = "Dynamic colors",
                primaryLight = Color(context.getColor(android.R.color.system_accent1_600)),
                primaryDark = Color(context.getColor(android.R.color.system_accent1_200)),
                secondaryLight = Color(context.getColor(android.R.color.system_accent2_600)),
                secondaryDark = Color(context.getColor(android.R.color.system_accent2_200)),
                tertiaryLight = Color(context.getColor(android.R.color.system_accent3_600)),
                tertiaryDark = Color(context.getColor(android.R.color.system_accent3_200)),
            )
        }
    }
}
