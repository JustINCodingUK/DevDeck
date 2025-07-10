package dev.justincodinguk.devdeck.core.deck_api.refs

import dev.justincodinguk.devdeck.core.deck_api.NoBuildException
import dev.justincodinguk.devdeck.core.deck_api.UnknownPackageManagerException
import dev.justincodinguk.devdeck.core.deck_api.UnsafeCommandException
import dev.justincodinguk.devdeck.core.deck_api.ext.isUnsafe
import kotlinx.serialization.Serializable

/**
 * Data class representing an installation reference.
 *
 * @param id The unique identifier of the installation reference of the form company.product
 * @param name The name of the binary to be installed
 * @param windows The [TargetPlatform] for Windows
 * @param macOs The [TargetPlatform] for macOS
 * @param linux The [TargetPlatform] for Linux
 * @param isDesktopApp Whether the binary is a desktop application
 */
@Serializable
data class InstallReference(
    val id: String,
    val name: String,
    val windows: TargetPlatform,
    val macOs: TargetPlatform,
    val linux: TargetPlatform,
    val isDesktopApp: Boolean = false
) {

    /**
     * Data class representing unique installation data for the current platform.
     *
     * @param isUrl Whether the result is a URL
     * @param result The result, which could be a URL or package ID for the current platform's package manager.
     * @param executablePath The path to the executable for the current platform
     * @param iconPath The path to the icon for the current platform
     */
    data class ReferenceResult(
        val isUrl: Boolean,
        val result: String,
        val executablePath: String,
        val iconPath: String? = null
    ) {

        /**
         * The directory containing the executable for the current platform.
         */
        val binDirectory
            get() : String {
                val splitChar = if (executablePath.contains("/")) "/" else "\\"
                val splitPath = executablePath.split(splitChar)
                val binDirectory = splitPath.subList(0, splitPath.size - 1).joinToString(splitChar)
                return binDirectory
            }
    }

    /**
     * Gets the [ReferenceResult] for the current platform.
     *
     * @param platform The current platform
     * @param forceUrl Whether to force the result to be a URL
     *
     * @return The [ReferenceResult] pertaining to [platform]
     * @throws NoBuildException If no build data is available for the current platform
     */
    fun getReferenceResultForPlatform(platform: Platform, forceUrl: Boolean = false): ReferenceResult {
        return when (platform.os) {
            OS.Windows -> buildReferenceResult(platform, windows, forceUrl)
            OS.MacOS -> buildReferenceResult(platform, macOs, forceUrl)
            OS.Linux -> buildReferenceResult(platform, linux, forceUrl)
        }
    }

    /**
     * Builds the [ReferenceResult] for the current platform.
     *
     * @param platform The current platform
     * @param targetPlatform The [TargetPlatform] binary data for the current platform
     * @param forceUrl Whether to force the result to be a URL
     *
     * @return The [ReferenceResult] pertaining to [platform]
     *
     * @throws NoBuildException If no build data is available for the current platform
     */
    private fun buildReferenceResult(
        platform: Platform,
        targetPlatform: TargetPlatform,
        forceUrl: Boolean = false
    ): ReferenceResult {
        val currentPackageManager =
            PackageManagerHelper.getInstallCommandPrefix(platform, isDesktopApp).split(" ").first()
        return with(targetPlatform) {
            if(!forceUrl) {
                for (packageId in packageIds) {
                    if (packageId.key == currentPackageManager) {
                        if (packageId.value.trim().isUnsafe()) throw UnsafeCommandException(id)

                        return@with ReferenceResult(
                            isUrl = false,
                            result = "$currentPackageManager ${packageId.value}",
                            executablePath = executablePath
                        )
                    } else continue
                }
            }
            try {
                return@with ReferenceResult(
                    isUrl = true,
                    result = if (platform.arch == Arch.X64) targetPlatform.binaryUrls.x64!! else targetPlatform.binaryUrls.aarch64!!,
                    executablePath = executablePath,
                    iconPath = targetPlatform.iconPath
                )
            } catch (e: Exception) {
                throw NoBuildException(id)
            }
        }
    }
}