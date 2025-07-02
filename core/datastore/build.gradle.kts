plugins {
    alias(libs.plugins.kotlinMultiplatform)
    `maven-publish`
}

kotlin {
    jvm("desktop")
    sourceSets {
        commonMain.dependencies {
            implementation(libs.mapdb)
        }
    }
}

publishing {
    publications.withType<MavenPublication>().named("kotlinMultiplatform") {
        groupId = "dev.justincodinguk"
        artifactId = "secure-mapdb"
        version = "1.0.0"
    }
}