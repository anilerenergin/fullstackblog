// LoginView.swift
import SwiftUI
struct LoginView: View {
    @ObservedObject private var viewModel = AuthViewModel()
  
   
    @State private var username = ""
    @State private var password = ""
    
    var body: some View {
        if viewModel.isLoggedIn {
            FeedView()
                .navigationBarHidden(true)
        } else {
            NavigationView {
                VStack {
                    TextField("Username", text: $username)
                        .padding()
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                        .autocapitalization(.none)
                    
                    SecureField("Password", text: $password)
                        .padding()
                        .textFieldStyle(RoundedBorderTextFieldStyle())
                    
                    Button("Login") {
                        let loginRequest = LoginRequest(name: username, password: password)
                        viewModel.login(loginRequest: loginRequest)
          
                    }
                    .padding()
                    
                    Text(viewModel.authResult)
                        .padding()
                        .foregroundColor(viewModel.authResult.contains("successful") ? .green : .red)
                        .multilineTextAlignment(.center)
                }
                .padding()
                .navigationBarTitle("Login")
                .navigationBarHidden(true)
            }
            .onReceive(viewModel.$isLoggedIn) { newValue in
                if newValue {
                    // Navigate programmatically when isLoggedIn changes
                    // Use FeedView as the root view of the NavigationView
                    NavigationLink(destination: FeedView(), isActive: .constant(true)) {
                        EmptyView()
                    }
                    .navigationBarHidden(true)
                }
            }
        }
    }
    
    struct LoginView_Previews: PreviewProvider {
        static var previews: some View {
            LoginView()
        }
    }
}
