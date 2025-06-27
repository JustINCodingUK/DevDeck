package dev.justincodinguk.devdeck.features.auth

import DevDeck.features.auth.BuildConfig
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.justincodinguk.credence.oauth_github.GithubOAuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _emailText = MutableStateFlow("")
    val emailText = _emailText.asStateFlow()

    private val _passwordText = MutableStateFlow("")
    val passwordText = _passwordText.asStateFlow()

    private val githubOAuthService = GithubOAuthService(
        clientId = BuildConfig.GITHUB_OAUTH_CLIENTID,
        clientSecret = BuildConfig.GITHUB_OAUTH_CLIENT_SECRET,
        redirectUri = BuildConfig.GITHUB_OAUTH_REDIRECT_URI
    )

    fun loginWithGithub() {
        viewModelScope.launch {
            githubOAuthService.authenticate()
        }
    }

    fun updateEmailText(newText: String) {
        viewModelScope.launch {
            _emailText.emit(newText)
        }
    }

    fun updatePasswordText(newText: String) {
        viewModelScope.launch {
            _passwordText.emit(newText)
        }
    }
}