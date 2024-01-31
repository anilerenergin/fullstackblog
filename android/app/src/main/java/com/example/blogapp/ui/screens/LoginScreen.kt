import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.blogapp.data.network.RetrofitInstance
import com.example.blogapp.data.repository.AuthRepository

// LoginScreen
@Composable
fun LoginScreen(navController:NavController,snackbarHostState:SnackbarHostState) {
    val retrofit = RetrofitInstance
    val authRepository = AuthRepository(authService = retrofit.userApiService)
    val authViewModel = AuthViewModel(authRepository = authRepository)
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authState by authViewModel.authState.observeAsState()
    val focusManager = LocalFocusManager.current

    // Pass

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Username") },
                leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(
                        focusDirection = FocusDirection.Next,
                    )
                })
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(

                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = {

                    focusManager.clearFocus(true)
                    loginAction(authViewModel,username, password)
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoginButton(authViewModel, username, password)

            LaunchedEffect(authState) {
                println("LaunchedEffect triggered with authState: $authState")
                when (authState) {
                    is AuthState.Success -> {
                        println("view auth success message")
                        snackbarHostState.showSnackbar("Successfully logged in as ${(authState as AuthState.Success).user.name}")
                        navController.navigate("FeedScreen")
                    }
                    is AuthState.WrongPassword -> {
                        snackbarHostState.showSnackbar("Wrong password")
                    }
                    is AuthState.NotFound -> {
                        snackbarHostState.showSnackbar("User not found")
                    }
                    else -> {

                    }
                }
            }
        }
    }
}


@Composable
fun LoginButton(authViewModel: AuthViewModel, username: String, password: String) {
    Button(
        onClick = {
            loginAction(authViewModel,username, password)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(Icons.Default.Send, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Login")
    }
}
fun loginAction(authViewModel:AuthViewModel,username:String,password:String){
    authViewModel.login(LoginRequest(name = username, password = password))


}
@Preview(name = "LoginScreen")
@Composable
private fun PreviewLoginScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    LoginScreen(navController,snackbarHostState)
}