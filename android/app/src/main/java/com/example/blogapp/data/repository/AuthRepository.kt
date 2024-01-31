package com.example.blogapp.data.repository
import AuthResult
import LoginRequest
import com.example.blogapp.data.network.AuthService
import retrofit2.HttpException
class AuthRepository(private val authService: AuthService) {
     suspend  fun login(loginRequest: LoginRequest): AuthResult {
        return try {
            println(loginRequest.name)
            println(loginRequest.password)
            val user = authService.login(loginRequest)
            AuthResult.Success(user)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> AuthResult.WrongPassword(e.message())
                404 -> AuthResult.NotFound(e.message())
                else ->AuthResult.Error("Login failed: ${e.message}")
            }
        } catch (e: Exception) {
            AuthResult.Error("Login failed: ${e.message}")
        }
    }

}
