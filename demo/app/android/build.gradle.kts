import com.michaelflisar.kmpdevtools.Targets
import com.michaelflisar.kmpdevtools.BuildFileUtil
import com.michaelflisar.kmpdevtools.core.Target
import com.michaelflisar.kmpdevtools.configs.*
import com.michaelflisar.kmpdevtools.setupDependencies

plugins {
    // kmp + app/library
    alias(libs.plugins.android.application)
    // org.jetbrains.kotlin
    alias(libs.plugins.jetbrains.kotlin.compose)
    // org.jetbrains.compose
    alias(libs.plugins.jetbrains.compose)
    // docs, publishing, validation
    // --
    // build tools
    alias(mflisar.plugins.kmpdevtools.buildplugin)
}

// ------------------------
// Setup
// ------------------------

val module = AppModuleConfig.read(project)

val buildTargets = Targets(Target.ANDROID)

val androidConfig = AndroidAppConfig(
    compileSdk = app.versions.compileSdk,
    minSdk = app.versions.minSdk,
    targetSdk = app.versions.targetSdk
)

// -------------------
// Kotlin
// -------------------

// android configuration
android {

    compileOptions {
        //isCoreLibraryDesugaringEnabled = true
    }

    BuildFileUtil.setupAndroidApp(
        appModuleConfig = module,
        androidAppConfig = androidConfig,
        generateResAppName = true,
        buildConfig = true,
        checkDebugKeyStoreProperty = true,
        setupBuildTypesDebugAndRelease = true
    )

    //defaultConfig {
    //    missingDimensionStrategy("platform", "play")
    //}
}

dependencies {

    // desugar
    //coreLibraryDesugaring(libs.desugar)

    implementation(libs.androidx.activity.compose)

    // Library
    implementation(project(":demo:shared"))
}