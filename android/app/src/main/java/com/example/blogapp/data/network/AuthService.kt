package com.example.blogapp.data.network

import LoginRequest
import User
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("login/")
    suspend fun login(@Body loginRequest: LoginRequest): User
}
