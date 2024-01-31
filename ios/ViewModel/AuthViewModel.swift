    // AuthViewModel.swift
    import Foundation

    class AuthViewModel: ObservableObject {
    
        @Published var authResult: String = ""
        @Published var isLoggedIn: Bool = false // Introduce isLoggedIn property
        private let authRepository = AuthRepository()

        func login(loginRequest: LoginRequest) {
            authRepository.login(loginRequest: loginRequest) { result in
                DispatchQueue.main.async {
                    switch result {
                    case .success(let user):
                        self.authResult = "Login successful. User: \(user.name)"
                        
                        self.isLoggedIn = true
                        // Set isLoggedIn to true upon successful login
                    case .failure(let error):
                        // Handle errors as before
                        if let networkError = error as? NetworkError {
                            switch networkError {
                            case .invalidURL:
                                self.authResult = "Invalid URL."
                            case .noData:
                                self.authResult = "No data received."
                            case .unknownError:
                                self.authResult = "Unknown error occurred."
                            }
                        } else if let authError = error as? AuthError {
                            switch authError {
                            case .wrongPassword:
                                self.authResult = "Wrong password."
                            case .userNotFound:
                                self.authResult = "User not found."
                            }
                        } else {
                            
                        }
                    }
                }
            }
        }
    }
