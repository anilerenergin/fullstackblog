package com.example.blogapp.data.repository

import com.example.blogapp.data.model.PostRequest
import com.example.blogapp.data.model.PostResult
import com.example.blogapp.data.network.PostService
import retrofit2.HttpException

class PostRepository(private val postService: PostService) {

    suspend fun getPosts(): PostResult {
        return try {
            val postsResponse = postService.getPosts()
            println("posts fetched : $postsResponse")
            PostResult.PostsLoaded(postsResponse)

        } catch (e: HttpException) {
            println("Failed to fetch posts: ${e.message()}")
            PostResult.Error("Failed to fetch posts: ${e.message()}")
        } catch (e: Exception) {
            println("Failed to fetch posts: ${e.message}")
            PostResult.Error("Failed to fetch posts: ${e.message}")
        }
    }

    suspend fun getPost(id: Int): PostResult {
        return try {
            val postResponse = postService.getPost(id)
            PostResult.SinglePostLoaded(postResponse)
        } catch (e: HttpException) {
            PostResult.Error("Failed to fetch post: ${e.message()}")
        } catch (e: Exception) {
            PostResult.Error("Failed to fetch post: ${e.message}")
        }
    }

    suspend fun addPost(post: PostRequest): PostResult {
        return try {
            println(post)
            val addPostResponse = postService.addPost(post)
            println(addPostResponse)
            PostResult.PostAdded(addPostResponse)
        } catch (e: HttpException) {
            PostResult.Error("Failed to add post http exception: ${e.message()}")
        } catch (e: Exception) {
            PostResult.Error("Failed to add post: ${e.message}")
        }
    }

    suspend fun deletePost(id: Int): PostResult {
        return try {
            postService.deletePost(id)
            PostResult.PostDeleted("Post deleted successfully")
        } catch (e: HttpException) {
            PostResult.Error("Failed to delete post: ${e.message()}")
        } catch (e: Exception) {
            PostResult.Error("Failed to delete post: ${e.message}")
        }
    }
}
