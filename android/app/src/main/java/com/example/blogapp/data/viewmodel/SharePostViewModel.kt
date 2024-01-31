package com.example.blogapp.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blogapp.data.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import com.example.blogapp.data.model.PostRequest
import com.example.blogapp.data.model.PostResult
import kotlinx.coroutines.launch

class SharePostViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _sharePostState = MutableLiveData<SharePostState>(SharePostState.Idle)
    val sharePostState: LiveData<SharePostState> get() = _sharePostState


    fun addPost(post: PostRequest) {
        println("post adding")
        println(post.dateTime)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("post adding 2")
                _sharePostState.postValue(SharePostState.Loading)
                when (val result = postRepository.addPost(post)) {

                    is PostResult.PostAdded -> {
                        _sharePostState.postValue(SharePostState.Success)
                        println("post share success")
                    }
                    is PostResult.Error -> {
                        _sharePostState.postValue(SharePostState.Error(result.message))
                        println(result.message)
                    }
                    else -> _sharePostState.postValue(SharePostState.Error("Unknown Error"))
                }
            } catch (e: Exception) {
                println("post adding 3")
                _sharePostState.postValue(SharePostState.Error("An unexpected error occurred."))
            }
        }
    }
    fun resetSharePostState() {
        _sharePostState.postValue(SharePostState.Idle)
    }
}

sealed class SharePostState {
    data object Idle : SharePostState()
    data object Loading : SharePostState()
    data object Success : SharePostState()
    data class Error(val message: String) : SharePostState()
}
