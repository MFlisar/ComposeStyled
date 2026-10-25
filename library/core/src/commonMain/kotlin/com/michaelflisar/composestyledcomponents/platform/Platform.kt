package com.michaelflisar.composestyledcomponents.platform

import androidx.compose.runtime.staticCompositionLocalOf
import com.michaelflisar.composestyledcomponents.classes.StyledLocalMissingException
// TODO
//expect fun currentPlatform(): Platform

fun currentPlatform(): Platform = Platform.Desktop(Platform.Desktop.OS.Windows)

sealed class Platform {
    object Android : Platform() {
        override fun toString() = "Android"
    }

    object IOS : Platform() {
        override fun toString() = "IOS"
    }

    data class Desktop(val os: OS) : Platform() {
        enum class OS {
            Windows,
            Mac,
            Unknown
        }

        override fun toString(): String {
            return "Desktop ($os)"
        }
    }

    object Web : Platform() {
        override fun toString() = "Web"
    }

    companion object {
        val ALL = listOf(
            Android,
            IOS,
            Desktop(Desktop.OS.Windows),
            Desktop(Desktop.OS.Mac),
            Web
        )
    }
}

internal val LocalPlatform = staticCompositionLocalOf<Platform> { throw StyledLocalMissingException("LocalPlatform") }