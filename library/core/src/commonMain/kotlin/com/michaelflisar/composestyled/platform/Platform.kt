package com.michaelflisar.composestyled.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import com.michaelflisar.composestyled.classes.StyledLocalMissingException
import com.michaelflisar.composestyled.themes.StyledContrastLevel

/** Returns the platform detected for the current target. */
expect fun currentPlatform(): Platform

/** Reads the system contrast preference when the target exposes one. */
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

    /** Whether this platform is a phone or tablet target. */
    fun isMobile(): Boolean = this == Android || this == IOS

    /** Whether this platform is a desktop target, excluding web. */
    fun isDesktop(): Boolean = this != Android && this != IOS && this != Wasm

    /** Whether this platform is desktop or web. */
    fun isDesktopOrWeb(): Boolean = isDesktop() || this == Wasm

    /** Whether this platform runs on the JVM. */
    fun isJvm(): Boolean =
        this == JvmWindows || this == JvmMacOS || this == JvmLinux || this == JvmUnknown

    /** Whether this platform is a native desktop target. */
    fun isNativeDesktop(): Boolean = this == MacOS

    /** Whether this platform runs on Windows. */
    fun isWindows(): Boolean = os == OS.Windows

    /** Whether this platform runs on macOS. */
    fun isMacOS(): Boolean = os == OS.Mac

    enum class OS { Windows, Mac, Linux, Android, IOS, Unknown }

    enum class Target { Android, IOS, Jvm, Web, Native }
}

internal val LocalPlatform =
    staticCompositionLocalOf<Platform> { throw StyledLocalMissingException("LocalPlatform") }
