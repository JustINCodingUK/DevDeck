package dev.justincodinguk.devdeck.core.datastore

import dev.justincodinguk.devdeck.core.datastore.security.Encryptor
import dev.justincodinguk.devdeck.core.datastore.security.SecurityOptions
import org.mapdb.DBMaker
import org.mapdb.Serializer
import java.io.File

class KeyValueDatastore(
    path: String,
    securityOptions: SecurityOptions
) {

    private val encryptor: Encryptor? = if(securityOptions.isEncryptionEnabled) {
        Encryptor(File(path), securityOptions.encryptionKey!!)
    } else null

    private val db = DBMaker.fileDB(path)
        .fileMmapEnableIfSupported()
        .closeOnJvmShutdown()
        .make()

    fun put(hashMapKey: String, key: String, value: String) {
        encryptor?.decrypt()
        db.hashMap(hashMapKey, Serializer.STRING, Serializer.STRING)
            .createOrOpen()[key] = value
        encryptor?.encrypt()
    }

    fun retrieve(hashMapKey: String, key: String): String? {
        encryptor?.encrypt()
        encryptor?.decrypt()
        val value = db.hashMap(hashMapKey, Serializer.STRING, Serializer.STRING)
            .createOrOpen()[key]
        encryptor?.encrypt()
        return value
    }

    fun clear(hashMapKey: String, key: String) {
        encryptor?.decrypt()
        db.hashMap(hashMapKey, Serializer.STRING, Serializer.STRING)
            .createOrOpen().remove(key)
        encryptor?.encrypt()
    }

    fun close() = db.close()
}