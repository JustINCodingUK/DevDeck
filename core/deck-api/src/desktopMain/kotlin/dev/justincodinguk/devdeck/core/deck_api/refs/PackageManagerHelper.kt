package dev.justincodinguk.devdeck.core.deck_api.refs

import dev.justincodinguk.devdeck.core.deck_api.UnknownPackageManagerException

object PackageManagerHelper {

    private val commonLinuxPmCommands = listOf(
        "apt install",
        "pacman -S",
        "dnf install",
        "apk add",
        "flatpak install flathub",
        "snap install --classic"
    )

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