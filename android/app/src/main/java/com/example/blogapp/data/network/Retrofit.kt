package com.example.blogapp.data.network

import com.example.blogapp.AppConfig.Companion.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val userApiService: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl("$baseUrl/user/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }

    val postApiService: PostService by lazy {
        Retrofit.Builder()
            .baseUrl("$baseUrl/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PostService::class.java)
    }
}
