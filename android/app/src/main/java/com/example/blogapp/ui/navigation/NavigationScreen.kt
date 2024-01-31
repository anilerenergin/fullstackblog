package com.example.blogapp.ui.navigation

import LoginScreen
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blogapp.data.network.RetrofitInstance
import com.example.blogapp.data.repository.PostRepository
import com.example.blogapp.data.viewmodel.PostViewModel
import com.example.blogapp.data.viewmodel.SharePostViewModel
import com.example.blogapp.ui.screens.PostScreen
import com.example.blogapp.ui.screens.SharePostScreen

@Composable
fun NavigationScreen(snackbarHostState:SnackbarHostState
) {
    val retrofit = RetrofitInstance
    val navController = rememberNavController()
    val postRepository = PostRepository(postService = retrofit.postApiService)
    val postViewModel = PostViewModel(postRepository = postRepository)
    val sharePostViewModel = SharePostViewModel(postRepository = postRepository)
    NavHost(navController = navController, startDestination = "LoginScreen") {
        composable("LoginScreen") { LoginScreen(navController, snackbarHostState) }
        composable("PostScreen") { PostScreen(navController,postViewModel) }
        composable("SharePostScreen") { SharePostScreen(navController,sharePostViewModel,snackbarHostState) }
    }
}


@Preview(name = "Navigation")
@Composable
private fun PreviewNavigation() {
    val snackbarHostState = remember { SnackbarHostState() }
    NavigationScreen(snackbarHostState)
}