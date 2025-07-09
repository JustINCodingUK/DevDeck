package dev.justincodinguk.devdeck.core.deck_api.refs

import kotlinx.serialization.Serializable

data class Platform(
    val os: OS,
    val arch: Arch
) {
    companion object {
        fun current(): Platform {
            val osName = System.getProperty("os.name")
            val os = if(osName.startsWith("Windows")) {
                OS.Windows
            } else if(osName.startsWith("Mac")) {
                OS.MacOS
            } else {
                OS.Linux
            }

            val osArch = System.getProperty("os.arch")
            val arch = if(osArch.contains("amd")) {
                Arch.X64
            } else {
                Arch.AARCH64
            }
            return Platform(os, arch)
        }
    }
}

@Serializable
data class TargetPlatform(
    val os: OS,
    val packageIds: Map<String, String> = emptyMap(),
    val binaryUrls: BinaryUrls,
    val executablePath: String,
    val iconPath: String? = null
)

@Serializable
data class BinaryUrls(
    val x64: String? = null,
    val aarch64: String? = null
)

enum class OS {
    Windows,
    MacOS,
    Linux;
}

enum class Arch {
    X64,
    AARCH64;

    override fun toString(): String {
        return super.toString().removePrefix("Arch.").lowercase()
    }
}

