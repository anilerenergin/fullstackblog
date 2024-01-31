package com.example.blogapp.ui.screens

import DateUtility.timePassed
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.blogapp.data.model.Post
import com.example.blogapp.data.network.RetrofitInstance
import com.example.blogapp.data.repository.PostRepository
import com.example.blogapp.data.viewmodel.PostState
import com.example.blogapp.data.viewmodel.PostViewModel
import com.example.blogapp.ui.theme.BlogApp
import com.example.blogapp.ui.ui.theme.AccentColor
import com.example.blogapp.ui.ui.theme.PrimaryColor

@Composable
fun PostScreen(navController: NavController,postViewModel: PostViewModel) {
    val posts by postViewModel.postState.collectAsState()

    LaunchedEffect(Unit) {
        postViewModel.getPosts()
    }
    BlogApp {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    containerColor = PrimaryColor,
                    onClick = {navController.navigate("SharePostScreen")},
                    content = { Icon(Icons.Default.Add, contentDescription = null) }
                )
            },
            content = { innerPadding ->
                when (posts) {
                    is PostState.Loading -> LoadingContent()
                    is PostState.Success -> PostList(
                        posts = (posts as PostState.Success).posts,
                        innerPadding, postViewModel = postViewModel
                    )

                    is PostState.Error -> ErrorContent(message = (posts as PostState.Error).message)
                    else -> EmptyContent()
                }
            }
        )
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun PostList(posts: List<Post>,padding :PaddingValues,postViewModel: PostViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(posts) { post ->
            PostCard(post = post, postViewModel = postViewModel)
        }
    }
}

@Composable
fun PostCard(post: Post,postViewModel:PostViewModel) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AccentColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row (
                Modifier.fillMaxWidth(),
horizontalArrangement = Arrangement.SpaceBetween

               ){

                Text(text = post.title, fontWeight = FontWeight.Bold, color = Color.Black)
                Icon( modifier = Modifier
                    .clickable {
                        postViewModel.deletePost(id = post.id)
                    },imageVector = Icons.Default.Delete, contentDescription ="Delete Post", tint = PrimaryColor, )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "By: ${post.composerName}", style = MaterialTheme.typography.titleMedium,color=Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = post.postMessage,color=Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = timePassed(post.dateTime), style = MaterialTheme.typography.titleMedium, color = Color.Gray)
        }
    }
}

@Composable
fun ErrorContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error: $message", color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun EmptyContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No posts available.")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostScreen() {
    val navController = rememberNavController()
    val retrofit = RetrofitInstance
    val postRepository = PostRepository(postService = retrofit.postApiService)
    val postViewModel = PostViewModel(postRepository = postRepository)
    PostScreen(navController,postViewModel)
}
