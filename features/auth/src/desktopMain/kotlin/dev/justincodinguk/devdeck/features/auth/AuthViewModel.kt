package dev.justincodinguk.devdeck.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.justincodinguk.devdeck.core.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    fun loginWithGithub() {
        viewModelScope.launch {
            _authState.emit(
                authState.value.copy(authStatus=AuthStatus.WAITING)
            )
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

    fun updateEmailText(newText: String) {
        viewModelScope.launch {
            _authState.emit(
                authState.value.copy(
                    emailText = newText
                )
            )
        }
    }

    fun updatePasswordText(newText: String) {
        viewModelScope.launch {
            _authState.emit(
                authState.value.copy(
                    passwordText = newText
                )
            )
        }
    }
}