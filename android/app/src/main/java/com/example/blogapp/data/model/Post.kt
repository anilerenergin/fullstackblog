package com.example.blogapp.data.model

import java.util.Date

data class Post(
    val id: Int,
    val composerName: String,
    val title: String,
    val postMessage: String,
    val dateTime: Date
)// AuthResult sealed class to represent different authentication outcomes

sealed class PostResult {
    data class PostsLoaded(val posts: List<Post>) : PostResult()
    data class PostAdded(val post: Post) : PostResult()
    data class SinglePostLoaded(val post: Post) : PostResult()
    data class Error(val message: String) : PostResult()
    data class PostDeleted(val message: String) : PostResult()
}
data class PostRequest(
    val composerName: String,
    val title: String,
    val postMessage: String,
    val dateTime: String
)// AuthResult sealed class to represent different authentication outcomes
