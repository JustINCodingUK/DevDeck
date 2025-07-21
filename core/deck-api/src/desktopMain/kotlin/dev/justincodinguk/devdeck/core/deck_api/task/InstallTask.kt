package dev.justincodinguk.devdeck.core.deck_api.task

import dev.justincodinguk.devdeck.core.deck_api.InstallException
import dev.justincodinguk.devdeck.core.deck_api.ext.runAsCommand
import dev.justincodinguk.devdeck.core.deck_api.refs.BinaryUrls
import dev.justincodinguk.devdeck.core.deck_api.refs.InstallReference
import dev.justincodinguk.devdeck.core.deck_api.refs.OS
import dev.justincodinguk.devdeck.core.deck_api.refs.PackageManagerHelper
import dev.justincodinguk.devdeck.core.deck_api.refs.Platform
import dev.justincodinguk.devdeck.core.deck_api.refs.TargetPlatform
import dev.justincodinguk.devdeck.core.deck_api.refs.InstallReference.ReferenceResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.timeout
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.jvm.javaio.copyTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Files
import java.util.zip.ZipFile

/**
 * Implementation of [Task] to install a binary from an installation reference, via the system package manager or manual installation.
 *
 * @param platform Current platform.
 * @param installReference The reference of the binary to be installed.
 * @param version The version of the binary to be installed, if not empty then would force a URL installation even if the binary can be resolved by the system package manager.
 * @param httpClient The client to use for making GET requests
 */
class InstallTask private constructor(
    private val platform: Platform,
    private val installReference: InstallReference,
    private val version: String,
    private val httpClient: HttpClient,
) : Task {

    /** The [ReferenceResult] object for the installation reference of the current platform */
    private val refResult = installReference.getReferenceResultForPlatform(platform)

    /** The cache directory of DevDeck */
    private val directory = "${System.getProperty("user.home")}/.devdeck/"

    companion object {
        
        /**
         * Creates an [InstallTask] for multiple packages to be installed via the system package manager
         *
         * @param platform The current platform
         * @param installReferences List of installation references to be acted upon
         * @param httpClient The client to use for making GET requests
         *
         * @return An [InstallTask] to install multiple packages via a single command
         */
        fun multiplePackages(
            platform: Platform,
            installReferences: List<InstallReference>,
            httpClient: HttpClient
        ): InstallTask {
            var packageManagerString =
                installReferences.first().getReferenceResultForPlatform(platform).result
            if (installReferences.size != 1) {
                installReferences.subList(1, installReferences.size - 1).forEach {
                    val packageName =
                        it.getReferenceResultForPlatform(platform).result.split(" ").last()
                    packageManagerString += " $packageName"
                }
            }

            var taskName = ""
            installReferences.forEach { taskName += "${it.name}, " }
            val currentPkgManager =
                PackageManagerHelper.getInstallCommandPrefix(platform, false).split(" ").first()
            val installReference = InstallReference(
                id = "pkg-manager-multiple",
                name = "${taskName.trim().removeSuffix(",")} via System Package Manager",
                windows = TargetPlatform(
                    OS.Windows,
                    mapOf(currentPkgManager to packageManagerString),
                    BinaryUrls(),
                    ""
                ),
                macOs = TargetPlatform(
                    OS.MacOS,
                    mapOf(currentPkgManager to packageManagerString),
                    BinaryUrls(),
                    ""
                ),
                linux = TargetPlatform(
                    OS.Linux,
                    mapOf(currentPkgManager to packageManagerString),
                    BinaryUrls(),
                    ""
                )
            )

            return InstallTask(platform, installReference, "", httpClient)
        }
    }

    override val name = "Install ${installReference.name} in $directory"

    private val logger = LoggerFactory.getLogger("InstallTask")

    /**
     * Executes the [InstallTask].
     *
     * First checks if the binary is to be installed via the package manager or not.
     * If not, the binary is downloaded and installed manually.
     */
    override suspend fun execute() {
        logger.info(name)
        if (!refResult.isUrl && version.isEmpty()) {
            withContext(Dispatchers.IO) {
                val command =
                    if (platform.os == OS.Linux) "pkexec ${refResult.result}" else refResult.result
                ProcessBuilder(command.split(" ")).start()
            }
        } else {
            val refResult = installReference.getReferenceResultForPlatform(platform, true)
            val url = refResult.result.replace("\${version}", version)
            File("$directory/bin/downloads").mkdirs()
            val fileName = url.split("/").last()
            val fileExtension = fileName.split(".").last()

            val file = File("$directory/bin/downloads/$fileName")
            httpClient.prepareGet(url) {
                timeout {
                    requestTimeoutMillis = HttpTimeoutConfig.INFINITE_TIMEOUT_MS
                    socketTimeoutMillis = 10 * 60 * 1000
                }
            }.execute { response ->
                val channel: ByteReadChannel = response.bodyAsChannel()

                file.outputStream().use {
                    channel.copyTo(it)
                }
            }

            val binDirectory = File("$directory/bin/${installReference.id}/").apply { mkdirs() }

            withContext(Dispatchers.IO) {
                when (fileExtension) {
                    "zip" -> ZipFile(file.absolutePath).install(binDirectory)
                    "dmg" -> file.installAsDmg()
                    "gz" -> file.installAsTar(binDirectory)
                    "xz" -> file.installAsTar(binDirectory)
                }
            }
        }
    }

    /**
     * Utility extension function to unzip a zip file to a directory
     *
     * @param directory The directory to unzip the file to
     */
    private fun ZipFile.install(directory: File) {
        val zipEntries = entries().asSequence()
        var rootFolder = "/"
        if (zipEntries.first().isDirectory) {
            rootFolder = zipEntries.first().name
        }
        zipEntries.forEach { entry ->
            val outFile = File(directory, entry.name)
            if (entry.isDirectory) {
                outFile.mkdirs()
            } else {
                outFile.parentFile.mkdirs()
                getInputStream(entry).use { input ->
                    outFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
        setupEnvVariables(rootFolder)
        if (installReference.isDesktopApp) createDesktopShortcut(rootFolder)
    }

    /**
     * Utility extension function to mount and install a .dmg file on a macOS system
     */
    private fun File.installAsDmg() {
        val attachProcess = ProcessBuilder("hdiutil attach $absolutePath -nobrowse".split(" "))
            .redirectErrorStream(true)
            .start()
        val mountOutput = attachProcess.inputStream.bufferedReader().readText()
        attachProcess.waitFor()

        val mountPoint = Regex("/Volumes/[^\\n]*").find(mountOutput)?.value
            ?: throw InstallException(
                installReference.id,
                "Could not find mount point in output:\n$mountOutput"
            )

        val app = File(mountPoint).listFiles()?.find { it.name.endsWith(".app") }
            ?: throw InstallException(installReference.id, "No .app found in mounted volume.")

        ProcessBuilder("cp -R ${app.absolutePath} /Applications/".split(" "))
            .inheritIO()
            .start()
            .waitFor()

        ProcessBuilder("hdiutil detach $mountPoint".split(" "))
            .inheritIO()
            .start()
            .waitFor()
    }

    /**
     * Utility extension function to extract a .tar.gz or .tar.xz file to a directory
     */
    private fun File.installAsTar(directory: File) {
        val flags = when {
            absolutePath.endsWith(".tar.gz") -> "-xzf"
            absolutePath.endsWith(".tar.xz") -> "-xJf"
            else -> throw InstallException(installReference.id, "Unsupported format: $absolutePath")
        }

        val process = ProcessBuilder("tar", flags, absolutePath, "-C", directory.absolutePath)
            .inheritIO()
            .start()
        var rootFolder = "/"
        val fileCount = File(directory.absolutePath).listFiles()?.size
        if (fileCount == 1) {
            rootFolder += File(directory.absolutePath).listFiles()?.first()?.name
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) throw InstallException(
            installReference.id,
            "Extraction failed with code $exitCode"
        )
        setupEnvVariables(rootFolder)
        if (installReference.isDesktopApp) createDesktopShortcut(rootFolder)
    }

    /**
     * Sets up the environment variables for a new installation for the current platform.
     *
     * @param rootFolder The root folder of the archive
     */
    private fun setupEnvVariables(rootFolder: String) {
        val executableDirectory =
            "${File(directory).absolutePath}/bin/${installReference.id}/${
                refResult.binDirectory.replace(
                    "/*",
                    rootFolder
                )
            }/"
        if (platform.os == OS.Linux) {

            val shellConfig = File(System.getProperty("user.home"), ".bash_profile")
            val exportLine = "export PATH=\"\$PATH:$executableDirectory\""

            if (shellConfig.readText().contains(exportLine)) {
                return
            }

            shellConfig.appendText("\n$exportLine\n")
            "source ${shellConfig.absolutePath}".runAsCommand()
        } else if (platform.os == OS.Windows) {
            ProcessBuilder(
                "setx PATH \"%PATH%;${
                    executableDirectory.replace(
                        "/",
                        "\\"
                    )
                }".split(" ")
            )
                .start()
                .waitFor()
        }
    }

    /**
     * Creates a desktop shortcut and application menu entry for the current installation.
     *
     * @param rootFolder The root folder of the archive
     */
    private fun createDesktopShortcut(rootFolder: String) {
        val executablePath =
            "${File(directory).absolutePath}/bin/${installReference.id}/${
                refResult.executablePath.replace(
                    "/*",
                    rootFolder
                )
            }/"
        val iconPath =
            "${File(directory).absolutePath}/bin/${installReference.id}/${
                refResult.iconPath?.replace(
                    "/*",
                    rootFolder
                )
            }/"
        if (platform.os == OS.Windows) {

            val desktopPath = System.getenv("USERPROFILE") + "\\Desktop\\$name.lnk"

            val vbs = buildString {
                appendLine("Set oWS = WScript.CreateObject(\"WScript.Shell\")")
                appendLine("sLinkFile = \"$desktopPath\"")
                appendLine("Set oLink = oWS.CreateShortcut(sLinkFile)")
                appendLine("oLink.TargetPath = \"${executablePath}\"")
                refResult.iconPath?.let {
                    appendLine("oLink.IconLocation = \"$iconPath\"")
                }
                appendLine("oLink.Save")
            }

            val vbsFile = Files.createTempFile("shortcut", ".vbs").toFile()
            vbsFile.writeText(vbs)

            ProcessBuilder("wscript", vbsFile.absolutePath)
                .inheritIO()
                .start()
                .waitFor()

            vbsFile.delete()

        } else if (platform.os == OS.Linux) {

            val desktopFileContents = buildString {
                appendLine("[Desktop Entry]")
                appendLine("name=${installReference.name}")
                appendLine("exec=${executablePath}")
                appendLine("icon=${iconPath}")
            }

            val userHome = System.getProperty("user.home")

            val desktopFile = File("$userHome/Desktop/${installReference.name}.desktop").apply {
                createNewFile()
            }

            val applicationMenuFile =
                File(
                    "$userHome/.local/share/applications/${
                        installReference.id.replace(".", "_").replace("-", "_")
                    }.desktop"
                ).apply {
                    createNewFile()
                }

            desktopFile.writeText(desktopFileContents)
            applicationMenuFile.writeText(desktopFileContents)
        }
    }

    /**
     * Builder class for [InstallTask] with a single package
     */
    class Builder : Task.Builder<InstallTask> {
        lateinit var platform: Platform
        lateinit var installReference: InstallReference
        var version: String = ""
        lateinit var httpClient: HttpClient

        override fun build() = InstallTask(platform, installReference, version, httpClient)
    }

    /**
     * Builder class for [InstallTask] with multiple packages
     */
    class MultiplePackagesBuilder : Task.Builder<InstallTask> {
        lateinit var platform: Platform
        lateinit var installReferences: List<InstallReference>
        lateinit var httpClient: HttpClient

        override fun build() = multiplePackages(platform, installReferences, httpClient)
    }

}