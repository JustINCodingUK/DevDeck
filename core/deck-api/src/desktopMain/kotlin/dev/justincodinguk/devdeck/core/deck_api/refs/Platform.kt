package dev.justincodinguk.devdeck.core.deck_api.refs

import kotlinx.serialization.Serializable

/**
 * Data class representing a platform.
 *
 * @param os The operating system
 * @param arch The architecture
 */
data class Platform(
    val os: OS,
    val arch: Arch
) {
    companion object {
        /**
         * Gets the current platform.
         *
         * @return The current platform
         */
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

/**
 * Data class representing a data for an installation reference for a specific target platform
 *
 * @param os The operating system
 * @param packageIds The package IDs of the binary for various package managers of the current platform.
 * @param binaryUrls The binary URLs for the current platform
 * @param executablePath The path to the executable for the current platform
 * @param iconPath The path to the icon for the current platform
 */
@Serializable
data class TargetPlatform(
    val os: OS,
    val packageIds: Map<String, String> = emptyMap(),
    val binaryUrls: BinaryUrls,
    val executablePath: String,
    val iconPath: String? = null
)

/**
 * Data class representing binary URLs for a various architectures.
 *
 * @param x64 The binary URL for an AMD64 architecture
 * @param aarch64 The binary URL for an ARM64 architecture
 */
@Serializable
data class BinaryUrls(
    val x64: String? = null,
    val aarch64: String? = null
)

/**
 * Enum representing an operating system.
 */
enum class OS {
    Windows,
    MacOS,
    Linux;
}

/**
 * Enum representing an architecture.
 */
enum class Arch {
    X64,
    AARCH64;

    override fun toString(): String {
        return super.toString().removePrefix("Arch.").lowercase()
    }
}

