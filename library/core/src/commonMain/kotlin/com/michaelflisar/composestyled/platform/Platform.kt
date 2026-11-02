package com.michaelflisar.composestyled.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.michaelflisar.composestyled.classes.StyledLocalMissingException
import com.michaelflisar.composestyled.themes.StyledContrastLevel

expect fun currentPlatform(): Platform

@Composable
expect fun platformSystemContrastLevel(): StyledContrastLevel

/**
 * Represents a supported target platform and its operating system.
 */
enum class Platform(val os: OS, val target: Target) {
    Android(OS.Android, Target.Android),
    IOS(OS.IOS, Target.IOS),
    Wasm(OS.Unknown, Target.Web),
    JvmWindows(OS.Windows, Target.Jvm),
    JvmMacOS(OS.Mac, Target.Jvm),
    JvmLinux(OS.Linux, Target.Jvm),
    JvmUnknown(OS.Unknown, Target.Jvm),
    MacOS(OS.Mac, Target.Native),
    ;

    fun isMobile(): Boolean = this == Android || this == IOS

    fun isDesktop(): Boolean = this != Android && this != IOS && this != Wasm

    fun isDesktopOrWeb(): Boolean = isDesktop() || this == Wasm

    fun isJvm(): Boolean =
        this == JvmWindows || this == JvmMacOS || this == JvmLinux || this == JvmUnknown

    fun isNativeDesktop(): Boolean = this == MacOS

    fun isWindows(): Boolean = os == OS.Windows

    fun isMacOS(): Boolean = os == OS.Mac

    enum class OS { Windows, Mac, Linux, Android, IOS, Unknown }

    enum class Target { Android, IOS, Jvm, Web, Native }
}

internal val LocalPlatform =
    staticCompositionLocalOf<Platform> { throw StyledLocalMissingException("LocalPlatform") }
