package dev.justincodinguk.devdeck.core.network.auth


import DevDeck.core.network.BuildConfig
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GithubAuthProvider
import dev.gitlive.firebase.auth.auth
import dev.justincodinguk.credence.oauth_github.GithubOAuthService
import dev.justincodinguk.devdeck.core.model.AccountType
import dev.justincodinguk.devdeck.core.model.DevDeckUser

internal class AccountManagerImpl : AccountManager {

    private val auth = Firebase.auth
    private var currentUser: DevDeckUser? = null

    override fun isSignedIn(): Boolean = auth.currentUser != null

    override fun getCurrentUser(): Result<DevDeckUser> {
        if(isSignedIn()) {
            if(currentUser == null) {
                val user = auth.currentUser!!
                val devDeckUser = DevDeckUser(
                    name = user.displayName!!,
                    email = user.email!!,
                    photoUrl = user.photoURL,
                    accountType = if(user.providerData.any { it.providerId == "github.com" }) AccountType.GITHUB else AccountType.DEV_DECK
                )
                currentUser = devDeckUser
                return Result.success(devDeckUser)
            } else {
                return Result.success(currentUser!!)
            }
        } else {
            return Result.failure(IllegalStateException("No user is signed in"))
        }
    }

    override suspend fun signIn(email: String, password: String) : Result<DevDeckUser> {
        val authResult = auth.signInWithEmailAndPassword(email, password)
        if(authResult.user != null) {
            val user = authResult.user!!
            val result = DevDeckUser(
                name = user.displayName!!,
                email = user.email!!,
                photoUrl = user.photoURL,
                accountType = AccountType.DEV_DECK
            )
            return Result.success(result)
        } else {
            return Result.failure(SignInException.invalidEmail())
        }
    }

    override suspend fun signInWithGithubToken(token: String) : Result<DevDeckUser> {
        val authResult = auth.signInWithCredential(GithubAuthProvider.credential(token))
        if(authResult.user != null) {
            val user = authResult.user!!
            val result = DevDeckUser(
                name = user.displayName!!,
                email = user.email!!,
                photoUrl = user.photoURL,
                accountType = AccountType.GITHUB
            )
            return Result.success(result)
        } else {
            return Result.failure(SignInException.invalidEmail())
        }
    }

    @Deprecated("Use Credence OAuth UI and pass the token to AccountManager.signInWithGithubToken(token) instead")
    override suspend fun signInWithGithub() : Result<DevDeckUser> {
        val githubOAuthService = GithubOAuthService(
            clientId = BuildConfig.GITHUB_OAUTH_CLIENTID,
            clientSecret = BuildConfig.GITHUB_OAUTH_CLIENT_SECRET,
            redirectUri = BuildConfig.GITHUB_OAUTH_REDIRECT_URI
        )

        val result = githubOAuthService.authenticate()
        if(result.isSuccess) {
            val user = result.getOrNull()!!
            auth.signInWithCredential(GithubAuthProvider.credential(user.token))
            val devDeckUser = DevDeckUser(
                name = user.name,
                email = user.email,
                photoUrl = user.avatarUrl,
                accountType = AccountType.GITHUB
            )
            return Result.success(devDeckUser)
        } else {
            return Result.failure(result.exceptionOrNull()!!)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        currentUser = null
    }
}