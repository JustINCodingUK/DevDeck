package dev.justincodinguk.devdeck.firebase

import DevDeck.composeApp.BuildConfig
import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseOptions
import com.google.firebase.FirebasePlatform
import com.google.firebase.initialize
import dev.justincodinguk.devdeck.core.datastore.KeyValueDatastore
import dev.justincodinguk.devdeck.core.datastore.security.SecurityOptions
import org.slf4j.LoggerFactory
import java.io.File

object Firebase {
    private val logger = LoggerFactory.getLogger("Firebase")
    private val userHome = System.getProperty("user.home")
    private val appDir = File(userHome, ".devdeck").apply { mkdirs() }
    private val datastoreFile = File(appDir, "datastore.db")

    private val securityOptions = SecurityOptions(
        isEncryptionEnabled = true,
        encryptionKey = BuildConfig.DB_ENCRYPTION_KEY
    )

    val datastore = KeyValueDatastore(datastoreFile.absolutePath, securityOptions)

    fun init() {
        FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {
            override fun clear(key: String) = datastore.clear("firebase", key)

            override fun log(msg: String) = logger.info(msg)

            override fun retrieve(key: String) = datastore.retrieve("firebase", key)

            override fun store(key: String, value: String) = datastore.put("firebase", key, value)
        })

        val firebaseOptions = FirebaseOptions.Builder()
            .setProjectId(BuildConfig.FIREBASE_PROJECT_ID)
            .setApplicationId(BuildConfig.FIREBASE_APP_ID)
            .setApiKey(BuildConfig.FIREBASE_API_KEY)
            .build()

        Firebase.initialize(Application(), firebaseOptions)
    }
}