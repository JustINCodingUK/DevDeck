import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildconfig)
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

buildConfig {
    buildConfigField("GITHUB_OAUTH_CLIENTID", ghClientId)
    buildConfigField("GITHUB_OAUTH_CLIENT_SECRET", ghClientSecret)
    buildConfigField("GITHUB_OAUTH_REDIRECT_URI", ghRedirectUri)
}

configurations.all {
    resolutionStrategy {
        cacheChangingModulesFor(0, "seconds")
    }
}

kotlin {
    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.firebase.auth)

            implementation(libs.koin.core)

            implementation(projects.core.di)
            implementation(projects.core.model)

            implementation("dev.justincodinguk.credence:oauth-github:1.0.0-dev07") { isChanging = true }
        }
    }
}
