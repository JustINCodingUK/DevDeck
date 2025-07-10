package dev.justincodinguk.devdeck.core.deck_api.refs

import dev.justincodinguk.devdeck.core.deck_api.UnknownPackageManagerException

/**
 * Helper object for managing package managers.
 */
object PackageManagerHelper {

    private val commonLinuxPmCommands = listOf(
        "apt install",
        "pacman -S",
        "dnf install",
        "apk add",
        "flatpak install flathub",
        "snap install --classic"
    )

    /**
     * Gets the install command prefix for the specified platform's package manager.
     *
     * @param platform The platform to get the install command prefix for
     * @param isDesktopApp Whether the binary is a desktop application
     *
     * @return The install command prefix for the specified platform's package manager
     */
    fun getInstallCommandPrefix(
        platform: Platform,
        isDesktopApp: Boolean
    ): String {
        return when (platform.os) {
            OS.Windows -> "winget install -e --id="
            OS.MacOS -> "brew install "+if(isDesktopApp) "--cask " else ""
            OS.Linux -> getLinuxPackageManager() + " "
        }
    }

    /**
     * Gets the install command prefix for Linux based operating systems.
     *
     * @return The install command prefix for the Linux based operating systems
     */
    private fun getLinuxPackageManager() : String {
        for(command in commonLinuxPmCommands) {
            val exitCode = ProcessBuilder("which ${command.split(" ").first()}".split(" "))
                .start()
                .waitFor()
            if(exitCode==0) {
                return command
            }
        }
        return ""
    }

}