import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.buildconfig)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if(file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val encryptionKey = localProperties.getProperty("db.encryption.key") as String

val firebaseProperties = Properties().apply {
    val file = rootProject.file("firebase.properties")
    if(file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val firebaseProjectId = firebaseProperties.getProperty("project.id") as String
val firebaseAppId = firebaseProperties.getProperty("app.id") as String
val firebaseApiKey = firebaseProperties.getProperty("api.key") as String

buildConfig {
    buildConfigField("String", "DB_ENCRYPTION_KEY", encryptionKey)
    buildConfigField("String", "FIREBASE_PROJECT_ID", firebaseProjectId)
    buildConfigField("String", "FIREBASE_APP_ID", firebaseAppId)
    buildConfigField("String", "FIREBASE_API_KEY", firebaseApiKey)
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            implementation(project(":core:ui"))
            implementation(project(":features:auth"))
            implementation(project(":core:logging"))
            implementation(project(":core:di"))
            implementation(project(":core:datastore"))
            implementation(compose.materialIconsExtended)
            implementation(libs.firebase.common)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.nav)
            implementation(libs.koin.core)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.compose.nav)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "dev.justincodinguk.devdeck.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Pkg)
            packageName = "dev.justincodinguk.devdeck"
            packageVersion = "1.0.0"
        }
    }
}
