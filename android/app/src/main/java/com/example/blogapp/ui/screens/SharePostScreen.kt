package com.example.blogapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.blogapp.data.model.PostRequest
import com.example.blogapp.data.network.RetrofitInstance
import com.example.blogapp.data.repository.PostRepository
import com.example.blogapp.data.viewmodel.SharePostState
import com.example.blogapp.data.viewmodel.SharePostViewModel
import com.example.blogapp.ui.ui.theme.PrimaryColor
import getCurrentDateTimeString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharePostScreen(
    navController: NavController,
    sharePostViewModel: SharePostViewModel,
    snackbarHostState:SnackbarHostState
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isNavigatingBack by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val sharePostState by sharePostViewModel.sharePostState.observeAsState()
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
        TopAppBar(
            title = { Text(text = "Share Post") },
            navigationIcon = {
                IconButton(onClick = {
                    if (!isNavigatingBack) {
                        // Set the flag to true to indicate back navigation is in progress
                        isNavigatingBack = true
                        sharePostViewModel.resetSharePostState()
                        navController.popBackStack()

                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)

        ) {
            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Title",
                        tint = PrimaryColor
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(
                        focusDirection = FocusDirection.Next,
                    )
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)


            )

            // Message field
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Message") },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Lock,
                        contentDescription = "Message",
                        tint = PrimaryColor
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(
                        focusDirection = FocusDirection.Next,
                    )
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Username field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Person,
                        contentDescription = "Username",
                        tint = PrimaryColor
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.clearFocus(true)
                }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Send button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        sharePostViewModel.addPost(PostRequest(title =title, postMessage = message, composerName = username, dateTime = getCurrentDateTimeString() ))
                              },
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = PrimaryColor)
                }
                LaunchedEffect(sharePostState) {
                    when (sharePostState) {
                        is SharePostState.Success -> {

                            snackbarHostState.showSnackbar("Post successfully shared")
                            sharePostViewModel.resetSharePostState()
                            navController.popBackStack()
                        }
                        is SharePostState.Error -> {
                            snackbarHostState.showSnackbar("Error occured while sharing post", duration = SnackbarDuration.Short )
                        }
                        else -> {

                        }
                    }
                }
            }
            }
        }
    }

@Preview(name = "SharePostScreen")
@Composable
private fun PreviewSharePostScreen() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val retrofit = RetrofitInstance
    val postRepository = PostRepository(postService = retrofit.postApiService)
    val sharePostViewModel = SharePostViewModel(postRepository = postRepository)
    SharePostScreen(navController, sharePostViewModel,snackbarHostState)
}
