package com.example.blogapp.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.model.PostRequest
import com.example.blogapp.data.model.PostResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _postState = MutableStateFlow<PostState>(PostState.Idle)
    val postState: StateFlow<PostState> get() = _postState

    fun getPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = postRepository.getPosts()) {
                    is PostResult.PostsLoaded -> _postState.value = PostState.Success(result.posts)
                    is PostResult.Error -> _postState.value = PostState.Error(result.message)
                    else -> _postState.value = PostState.Error("Unknown Error")
                }
            } catch (e: Exception) {
                _postState.value = PostState.Error("An unexpected error occurred.")
            }
        }
    }

    fun getPost(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _postState.value = PostState.Loading
                when (val result = postRepository.getPost(id)) {
                    is PostResult.SinglePostLoaded -> _postState.value =
                        PostState.SinglePost(result.post)
                    is PostResult.Error -> _postState.value = PostState.Error(result.message)
                    else -> _postState.value = PostState.Error("Unknown Error")
                }
            } catch (e: Exception) {
                _postState.value = PostState.Error("An unexpected error occurred.")
            }
        }
    }

    fun addPost(post: PostRequest) {
        println("post adding")
        println(post.dateTime)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("post adding 2")
                _postState.value = PostState.Loading
                when (val result = postRepository.addPost(post)) {

                    is PostResult.PostAdded -> _postState.value = PostState.SinglePost(result.post)
                    is PostResult.Error -> {
                        _postState.value = PostState.Error(result.message)
                        println(result.message)
                    }
                    else -> _postState.value = PostState.Error("Unknown Error")
                }
            } catch (e: Exception) {
                println("post adding 3")
                _postState.value = PostState.Error("An unexpected error occurred.")
            }
        }
    }

    fun deletePost(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                when (val result = postRepository.deletePost(id)) {
                    is PostResult.Error -> _postState.value = PostState.Error(result.message)
                    else -> {
                        // Successful deletion, reload the posts
                        getPosts()
                    }
                }
            } catch (e: Exception) {
                _postState.value = PostState.Error("An unexpected error occurred.")
            }
        }
    }

    fun resetPostState() {
        _postState.value = PostState.Idle
    }
}

sealed class PostState {
    data object Idle : PostState()
    data object Loading : PostState()
    data class Success(val posts: List<Post>) : PostState()
    data class SinglePost(val post: Post) : PostState()
    data class Error(val message: String) : PostState()
}
