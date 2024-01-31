// User model
data class User(
    val id: Int,
    val name: String,
    val password: String
)

// SignUpRequest model
data class SignUpRequest(
    val name: String,
    val password: String
)

// LoginRequest model
data class LoginRequest(
    val name: String,
    val password: String
)

// AuthResult sealed class to represent different authentication outcomes
sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
    data class WrongPassword(val message: String) : AuthResult()
    data class NotFound(val message: String) : AuthResult()
}
