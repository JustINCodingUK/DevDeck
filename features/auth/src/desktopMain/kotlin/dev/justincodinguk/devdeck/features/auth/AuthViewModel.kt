package dev.justincodinguk.devdeck.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.justincodinguk.devdeck.core.data.account.AccountRepository
import dev.justincodinguk.devdeck.core.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    init {
        if(accountRepository.isSignedIn()) {
            viewModelScope.launch {
                accountRepository.getCurrentUser().collect {
                    _authState.emit(
                        authState.value.copy(
                            authStatus = AuthStatus.LOGGED_IN,
                            user = it
                        )
                    )
                }
            }
        }
    }

    fun loginWithEmail() {
        viewModelScope.launch {
            val email = authState.value.emailText
            val password = authState.value.passwordText

            _authState.emit(authState.value.copy(authStatus = AuthStatus.WAITING))

            authRepository.signIn(email, password).collect {
                _authState.emit(
                    authState.value.copy(
                        authStatus = AuthStatus.LOGGED_IN,
                        user = it
                    )
                )
            }
        }
    }

    fun loginWithGithub() {
        viewModelScope.launch {
            _authState.emit(authState.value.copy(authStatus = AuthStatus.WAITING))
            authRepository.signInWithGithub().collect {
                _authState.emit(
                    authState.value.copy(
                        authStatus = AuthStatus.LOGGED_IN,
                        user = it
                    )
                )
            }
        }
    }

    fun registerEmailAccount() {
        viewModelScope.launch {
            _authState.emit(authState.value.copy(authStatus = AuthStatus.WAITING))
            val newUserFlow = authRepository.registerEmailAccount(
                authState.value.emailText,
                authState.value.passwordText,
                authState.value.displayNameText
            )
            _authState.emit(authState.value.copy(authStatus = AuthStatus.VERIFICATION))
            newUserFlow.collect {
                _authState.emit(
                    authState.value.copy(
                        authStatus = AuthStatus.LOGGED_IN,
                        user = it
                    )
                )
            }
        }
    }

    fun updateEmailText(newText: String) {
        viewModelScope.launch {
            _authState.emit(authState.value.copy(emailText = newText))
        }
    }

    fun updatePasswordText(newText: String) {
        viewModelScope.launch {
            _authState.emit(authState.value.copy(passwordText = newText))
        }
    }

    fun updateDisplayNameText(newText: String) {
        viewModelScope.launch {
            _authState.emit(authState.value.copy(displayNameText = newText))
        }
    }

    fun updateAuthMode(authMode: AuthMode) {
        viewModelScope.launch {
            _authState.emit(
                authState.value.copy(
                    authMode = authMode,
                    emailText = "",
                    passwordText = ""
                )
            )
        }
    }
}