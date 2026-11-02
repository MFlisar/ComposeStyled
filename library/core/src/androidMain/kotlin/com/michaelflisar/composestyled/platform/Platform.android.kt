package com.michaelflisar.composestyled.platform

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import com.michaelflisar.composestyled.themes.StyledContrastLevel
import java.util.concurrent.Executor

actual fun currentPlatform(): Platform = Platform.Android

@Composable
actual fun platformSystemContrastLevel(): StyledContrastLevel {
    val context = LocalContext.current
    val manager = remember(context) {
        context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
    }
    val contrast = remember(manager) { mutableStateOf(StyledContrastLevel.Normal) }
    DisposableEffect(manager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE || manager == null) {
            onDispose {}
        } else {
            contrast.value = manager.contrast.toStyledContrastLevel()
            val listener = UiModeManager.ContrastChangeListener { newContrast ->
                contrast.value = newContrast.toStyledContrastLevel()
            }
            val mainExecutor = Executor { command ->
                Handler(Looper.getMainLooper()).post(command)
            }
            manager.addContrastChangeListener(mainExecutor, listener)
            onDispose {
                manager.removeContrastChangeListener(listener)
            }
        }
    }
    return contrast.value
}

@Composable
actual fun rememberWindowEnvironment(): WindowEnvironment {
    val view = LocalView.current
    val activity = remember(view) { view.context.findActivity() }
    val environment = remember(view, activity) {
        mutableStateOf(activity?.readWindowEnvironment() ?: WindowEnvironment(WindowMode.Unknown))
    }
    DisposableEffect(view, activity) {
        val listener = android.view.View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            environment.value = activity?.readWindowEnvironment()
                ?: WindowEnvironment(WindowMode.Unknown)
        }
        view.addOnLayoutChangeListener(listener)
        onDispose {
            view.removeOnLayoutChangeListener(listener)
        }
    }
    SideEffect {
        environment.value = activity?.readWindowEnvironment()
            ?: WindowEnvironment(WindowMode.Unknown)
    }
    return environment.value
}

@Composable
@Suppress("DEPRECATION")
actual fun SystemBarStyleEffect(
    statusBar: SystemBarIconStyle?,
    navigationBar: SystemBarIconStyle?,
) {
    val view = LocalView.current
    val window = (view.parent as? DialogWindowProvider)?.window
        ?: LocalContext.current.findActivity()?.window
    val decorView = window?.decorView
    val legacySystemUiMask = buildList {
        if (statusBar != null) add(android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        if (navigationBar != null) add(android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    }.fold(0, Int::or)
    val appearanceMask = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        buildList {
            if (statusBar != null) add(android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
            if (navigationBar != null) add(android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
        }.fold(0, Int::or)
    } else {
        0
    }
    val baselineSystemUiFlags = remember(window, legacySystemUiMask) {
        decorView?.systemUiVisibility?.and(legacySystemUiMask) ?: 0
    }
    val baselineAppearance = remember(window, appearanceMask) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && window != null) {
            window.insetsController?.systemBarsAppearance?.and(appearanceMask) ?: 0
        } else {
            0
        }
    }
    val styleStatusBar = statusBar != null
    val styleNavigationBar = navigationBar != null
    // The caller draws a chosen color behind styled bars, so Android's contrast scrim would only hide it.
    DisposableEffect(window, styleStatusBar, styleNavigationBar) {
        val baselineStatusContrast = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && window != null) {
            window.isStatusBarContrastEnforced
        } else {
            null
        }
        val baselineNavigationContrast = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && window != null) {
            window.isNavigationBarContrastEnforced
        } else {
            null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && window != null) {
            if (styleStatusBar) window.isStatusBarContrastEnforced = false
            if (styleNavigationBar) window.isNavigationBarContrastEnforced = false
        }
        onDispose {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && window != null) {
                if (styleStatusBar && baselineStatusContrast != null) {
                    window.isStatusBarContrastEnforced = baselineStatusContrast
                }
                if (styleNavigationBar && baselineNavigationContrast != null) {
                    window.isNavigationBarContrastEnforced = baselineNavigationContrast
                }
            }
        }
    }
    DisposableEffect(window, legacySystemUiMask, appearanceMask) {
        onDispose {
            if (window != null && decorView != null && legacySystemUiMask != 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.setSystemBarsAppearance(
                        baselineAppearance,
                        appearanceMask,
                    )
                } else {
                    decorView.systemUiVisibility =
                        (decorView.systemUiVisibility and legacySystemUiMask.inv()) or baselineSystemUiFlags
                }
            }
        }
    }
    SideEffect {
        check((statusBar == null && navigationBar == null) || window != null) {
            "SystemBarStyleEffect requires a Compose view hosted by an Activity or Dialog."
        }
        if (window != null && decorView != null && legacySystemUiMask != 0) {
            val lightFlags = buildList {
                if (statusBar == SystemBarIconStyle.Dark) {
                    add(android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
                if (navigationBar == SystemBarIconStyle.Dark) {
                    add(android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }
            }.fold(0, Int::or)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val lightAppearance = buildList {
                    if (statusBar == SystemBarIconStyle.Dark) {
                        add(android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)
                    }
                    if (navigationBar == SystemBarIconStyle.Dark) {
                        add(android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
                    }
                }.fold(0, Int::or)
                window.insetsController?.setSystemBarsAppearance(lightAppearance, appearanceMask)
            } else {
                decorView.systemUiVisibility =
                    (decorView.systemUiVisibility and legacySystemUiMask.inv()) or lightFlags
            }
        }
    }
}

private fun Activity.readWindowEnvironment(): WindowEnvironment = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInPictureInPictureMode ->
        WindowEnvironment(WindowMode.PictureInPicture)
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && isInMultiWindowMode ->
        WindowEnvironment(WindowMode.MultiWindow)
    else -> WindowEnvironment(WindowMode.Normal)
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

private fun Float.toStyledContrastLevel(): StyledContrastLevel = when {
    // Match Android's nearest-level mapping for 0, 0.5 and 1; ties use the lower level.
    this <= 0.25f -> StyledContrastLevel.Normal
    this <= 0.75f -> StyledContrastLevel.Medium
    else -> StyledContrastLevel.High
}