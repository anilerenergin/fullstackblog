package com.example.blogapp.data.network
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.model.PostRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST

interface PostService {
    @GET("post")
    suspend fun getPosts(): List<Post>

    @GET("post/{id}/")
    suspend fun getPost(@Path("id") id: Int): Post

    @POST("post/add/")
    suspend fun addPost(@Body postRequest: PostRequest): Post

    @DELETE("post/{id}/")
    suspend fun deletePost(@Path("id") id: Int): Post
}
