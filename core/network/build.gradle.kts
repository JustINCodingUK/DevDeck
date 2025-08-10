import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildconfig)
    alias(libs.plugins.ksp)
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
val debugRaw = properties["debug"] as String
var debug = false
if(debugRaw == "true") {
    debug = true
}

buildConfig {
    buildConfigField("String","GITHUB_OAUTH_CLIENTID", ghClientId)
    buildConfigField("String", "GITHUB_OAUTH_CLIENT_SECRET", ghClientSecret)
    buildConfigField("String", "GITHUB_OAUTH_REDIRECT_URI", ghRedirectUri)
    buildConfigField("Boolean", "DEBUG", debug)
}

configurations.all {
    resolutionStrategy {
        cacheChangingModulesFor(0, "seconds")
    }
}

kotlin {
    jvm("desktop")

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.firebase.auth)
                implementation(libs.firebase.firestore)

                implementation(libs.koin.core)

                implementation(projects.core.model)
                implementation(projects.core.di)
                implementation("dev.justincodinguk.credence:oauth-github:1.0.0-dev07") { isChanging = true }
            }
        }

        val desktopMain by getting {
            resources.srcDir("build/generated/ksp/desktop/desktopMain/resources")
        }
    }
}


dependencies {
    add("kspDesktop", projects.core.di)
}
