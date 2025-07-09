package dev.justincodinguk.devdeck.core.datastore.security

import java.io.File
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.SecretKeySpec

class Encryptor(
    private val file: File,
    encryptionKey: String
) {

    private val keySpec = SecretKeySpec(java.util.HexFormat.of().parseHex(encryptionKey), "AES")
    private val cipher = Cipher.getInstance("AES")

    fun encrypt() {
        cipher.init(Cipher.ENCRYPT_MODE, keySpec)
        file.inputStream().use { inputStream ->
            File("${file.parent}/${file.name}.enc").outputStream().use { outputStream ->
                CipherOutputStream(outputStream, cipher).use { cipherOutputStream ->
                    inputStream.copyTo(cipherOutputStream)
                }
            }
        }
        file.delete()
    }

    fun decrypt() {
        cipher.init(Cipher.DECRYPT_MODE, keySpec)
        File("${file.absolutePath}.enc").inputStream().use { inputStream ->
            CipherInputStream(inputStream, cipher).use { cipherInputStream ->
                file.outputStream().use { outputStream ->
                    cipherInputStream.copyTo(outputStream)
                }
            }
        }
        File("${file.absolutePath}.enc").delete()
    }
}