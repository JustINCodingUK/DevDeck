import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.ksp)
}

repositories {
    mavenCentral()
    mavenLocal()
    google()
}

val properties = Properties().apply {
    val file = rootProject.file("local.properties")
    if(file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val ghClientId = properties["oauth.github.clientid"] as String
val ghClientSecret = properties["oauth.github.client_secret"] as String
val ghRedirectUri = properties["oauth.github.redirect_uri"] as String

java {
    targetCompatibility = JavaVersion.VERSION_1_8
}

buildConfig {
    buildConfigField("GITHUB_OAUTH_CLIENTID", ghClientId)
    buildConfigField("GITHUB_OAUTH_CLIENT_SECRET", ghClientSecret)
    buildConfigField("GITHUB_OAUTH_REDIRECT_URI", ghRedirectUri)
}

kotlin {
    jvm("desktop")

    sourceSets {
        commonMain {

            dependencies {
                implementation(libs.credence.oauth.github)
                implementation(libs.credence.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose.nav)
                implementation(libs.koin.core)
                implementation(project(":core:ui"))
                implementation(libs.kotlin.stdlib)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(compose.desktop.currentOs)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

