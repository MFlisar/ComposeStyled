package com.michaelflisar.composestyled.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetWidth
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import com.michaelflisar.composestyled.themes.StyledContrastLevel

actual fun currentPlatform(): Platform = Platform.IOS

@Composable
actual fun platformSystemContrastLevel(): StyledContrastLevel = StyledContrastLevel.Normal

@Composable
actual fun rememberWindowEnvironment(): WindowEnvironment {
    val controller = LocalUIViewController.current
    val environment = remember(controller) {
        mutableStateOf(controller.readWindowEnvironment())
    }
    DisposableEffect(controller) {
        val center = NSNotificationCenter.defaultCenter
        val observers = listOf(
            center.addObserverForName(
                name = "UIWindowSceneDidUpdateCoordinateSpaceNotification",
                `object` = null,
                queue = NSOperationQueue.mainQueue,
            ) {
                environment.value = controller.readWindowEnvironment()
            },
            center.addObserverForName(
                name = UIApplicationDidBecomeActiveNotification,
                `object` = null,
                queue = NSOperationQueue.mainQueue,
            ) {
                environment.value = controller.readWindowEnvironment()
            },
            center.addObserverForName(
                name = "ComposeStyledWindowEnvironmentChanged",
                `object` = null,
                queue = NSOperationQueue.mainQueue,
            ) {
                environment.value = controller.readWindowEnvironment()
            },
        )
        onDispose {
            observers.forEach(center::removeObserver)
        }
    }
    SideEffect {
        environment.value = controller.readWindowEnvironment()
    }
    return environment.value
}

@Composable
actual fun SystemBarStyleEffect(
    statusBar: SystemBarIconStyle?,
    navigationBar: SystemBarIconStyle?,
) {
    val host = LocalUIViewController.current.findStyledSystemBarHost()
    val originalStyle = remember(host) { host?.currentStyle }
    DisposableEffect(host) {
        onDispose {
            host?.setStatusBarStyle(originalStyle)
        }
    }
    SideEffect {
        if (statusBar != null) {
            checkNotNull(host) {
                "SystemBarStyleEffect requires a UIViewController wrapped with withStyledSystemBarStyleHost()."
            }.setStatusBarStyle(statusBar)
        }
    }
}

private fun UIViewController.findStyledSystemBarHost(): StyledSystemBarViewController? {
    var current: UIViewController? = this
    while (current != null) {
        if (current is StyledSystemBarViewController) return current
        current = current.parentViewController
    }
    return null
}

@OptIn(ExperimentalForeignApi::class)
private fun UIViewController.readWindowEnvironment(): WindowEnvironment {
    val window = view.window ?: return WindowEnvironment(WindowMode.Unknown)
    val scene = window.windowScene ?: return WindowEnvironment(WindowMode.Unknown)
    return if (window.isSmallerThan(scene)) {
        WindowEnvironment(WindowMode.MultiWindow)
    } else {
        WindowEnvironment(WindowMode.Normal)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun UIWindow.isSmallerThan(scene: UIWindowScene): Boolean {
    val windowBounds = bounds
    val screenBounds = scene.screen.bounds
    return CGRectGetWidth(windowBounds) + 1.0 < CGRectGetWidth(screenBounds) ||
        CGRectGetHeight(windowBounds) + 1.0 < CGRectGetHeight(screenBounds)
}

@OptIn(ExperimentalForeignApi::class)
internal class StyledSystemBarViewController internal constructor(
    private val childController: UIViewController,
) : UIViewController(nibName = null, bundle = null) {

    internal var currentStyle: SystemBarIconStyle? = null
        private set

    override fun viewDidLoad() {
        super.viewDidLoad()
        addChildViewController(childController)
        view.addSubview(childController.view)
        childController.didMoveToParentViewController(this)
    }

    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        childController.view.setFrame(view.bounds)
        NSNotificationCenter.defaultCenter.postNotificationName(
            "ComposeStyledWindowEnvironmentChanged",
            this,
        )
    }

    override fun preferredStatusBarStyle(): UIStatusBarStyle = when (currentStyle) {
        SystemBarIconStyle.Light -> UIStatusBarStyleLightContent
        SystemBarIconStyle.Dark -> UIStatusBarStyleDarkContent
        null -> super.preferredStatusBarStyle()
    }

    internal fun setStatusBarStyle(style: SystemBarIconStyle?) {
        if (currentStyle == style) return
        currentStyle = style
        setNeedsStatusBarAppearanceUpdate()
    }
}

/**
 * Wraps the app's Compose view controller so UIKit status-bar style requests can be forwarded
 * through the active view-controller hierarchy.
 */
fun UIViewController.withStyledSystemBarStyleHost(): UIViewController =
    StyledSystemBarViewController(this)