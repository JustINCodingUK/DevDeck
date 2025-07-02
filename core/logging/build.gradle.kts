plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        desktopMain.dependencies {
            api(libs.slf4j.api)
            api(libs.logback.classic)
        }
    }
}
