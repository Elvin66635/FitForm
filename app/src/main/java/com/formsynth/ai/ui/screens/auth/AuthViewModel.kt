package com.formsynth.ai.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.formsynth.ai.domain.repository.IAuthRepository
import com.formsynth.ai.domain.usecase.SignInUseCase
import com.formsynth.ai.domain.usecase.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val displayName: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val authRepository: IAuthRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    private val _isSignUp = MutableStateFlow(false)
    val isSignUp: StateFlow<Boolean> = _isSignUp.asStateFlow()
    
    init {
        // Check current authentication status
        viewModelScope.launch {
            authRepository.isAuthenticated.collect { isAuth ->
                _uiState.value = _uiState.value.copy(isAuthenticated = isAuth)
            }
        }
    }
    
    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null
        )
    }
    
    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null
        )
    }
    
    fun updateDisplayName(name: String) {
        _uiState.value = _uiState.value.copy(displayName = name)
    }
    
    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }
    
    fun toggleSignUpMode() {
        _isSignUp.value = !_isSignUp.value
        _uiState.value = _uiState.value.copy(
            emailError = null,
            passwordError = null,
            errorMessage = null
        )
    }
    
    fun signIn() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        
        if (!validateInput(email, password)) {
            return
        }
        
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            signInUseCase(email, password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Ошибка входа"
                    )
                }
        }
    }
    
    fun signUp() {
        val email = _uiState.value.email.trim()
        val password = _uiState.value.password
        val displayName = _uiState.value.displayName.trim()
        
        if (!validateInput(email, password, displayName)) {
            return
        }
        
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            signUpUseCase(email, password, displayName)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Ошибка регистрации"
                    )
                }
        }
    }
    
    fun signInWithGoogle(idToken: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            authRepository.signInWithGoogle(idToken)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Ошибка входа через Google"
                    )
                }
        }
    }
    
    fun setError(message: String) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
    }
    
    fun resetPassword() {
        val email = _uiState.value.email.trim()
        if (email.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                emailError = "Введите email для восстановления пароля"
            )
            return
        }
        
        viewModelScope.launch {
            authRepository.resetPassword(email)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Письмо для восстановления пароля отправлено на $email"
                    )
                }
                .onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = exception.message ?: "Ошибка восстановления пароля"
                    )
                }
        }
    }
    
    private fun validateInput(
        email: String,
        password: String,
        displayName: String = ""
    ): Boolean {
        var isValid = true
        
        if (_isSignUp.value && displayName.isEmpty()) {
            isValid = false
        }
        
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = _uiState.value.copy(
                emailError = "Введите корректный email"
            )
            isValid = false
        }
        
        if (password.isEmpty() || password.length < 6) {
            _uiState.value = _uiState.value.copy(
                passwordError = "Пароль должен содержать минимум 6 символов"
            )
            isValid = false
        }
        
        return isValid
    }
}



