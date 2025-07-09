package dev.justincodinguk.devdeck.core.deck_api.refs

import dev.justincodinguk.devdeck.core.deck_api.NoBuildException
import dev.justincodinguk.devdeck.core.deck_api.UnknownPackageManagerException
import dev.justincodinguk.devdeck.core.deck_api.UnsafeCommandException
import dev.justincodinguk.devdeck.core.deck_api.ext.isUnsafe
import kotlinx.serialization.Serializable

@Serializable
data class InstallReference(
    val id: String,
    val name: String,
    val windows: TargetPlatform,
    val macOs: TargetPlatform,
    val linux: TargetPlatform,
    val isDesktopApp: Boolean = false
) {

    data class ReferenceResult(
        val isUrl: Boolean,
        val result: String,
        val executablePath: String,
        val iconPath: String? = null
    ) {
        val binDirectory
            get() : String {
                val splitChar = if (executablePath.contains("/")) "/" else "\\"
                val splitPath = executablePath.split(splitChar)
                val binDirectory = splitPath.subList(0, splitPath.size - 1).joinToString(splitChar)
                return binDirectory
            }
    }

    fun getReferenceResultForPlatform(platform: Platform): ReferenceResult {
        return when (platform.os) {
            OS.Windows -> buildReferenceResult(platform, windows)
            OS.MacOS -> buildReferenceResult(platform, macOs)
            OS.Linux -> buildReferenceResult(platform, linux)
        }
    }

    private fun buildReferenceResult(
        platform: Platform,
        targetPlatform: TargetPlatform
    ): ReferenceResult {
        val currentPackageManager =
            PackageManagerHelper.getInstallCommandPrefix(platform, isDesktopApp).split(" ").first()
        return with(targetPlatform) {
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




