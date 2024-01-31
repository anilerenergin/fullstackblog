import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blogapp.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _authState = MutableLiveData<AuthState>(AuthState.Idle)
    val authState: LiveData<AuthState> get() = _authState
    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _authState.postValue(AuthState.Loading)
                when (val result =  authRepository.login(loginRequest)) {
                    is AuthResult.Success -> _authState.postValue(AuthState.Success(result.user))
                    is AuthResult.Error -> _authState.postValue(AuthState.Error(result.message))
                    is AuthResult.WrongPassword -> _authState.postValue(AuthState.WrongPassword(result.message))
                    is AuthResult.NotFound -> _authState.postValue(AuthState.NotFound(result.message))
                    else -> _authState.postValue(AuthState.Error("Unknown Error"))
                }
            } catch (e: Exception) {
                _authState.postValue(AuthState.Error("An unexpected error occurred."))
            }
        }
    }
    fun resetAuthState() {
        _authState.postValue(AuthState.Idle)
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
    data class WrongPassword(val message: String) : AuthState()
    data class NotFound(val message: String) : AuthState()
}
