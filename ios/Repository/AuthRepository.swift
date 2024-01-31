// AuthRepository.swift
import Foundation

class AuthRepository {
    private let baseURL: String

    init() {
        self.baseURL = AppConstants.baseURL
    }


    func login(loginRequest: LoginRequest, completion: @escaping (Result<User, Error>) -> Void) {
        guard let url = URL(string: "\(baseURL)/api/user/login") else {
            completion(.failure(NetworkError.invalidURL))
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            request.httpBody = try JSONEncoder().encode(loginRequest)
        } catch {
            completion(.failure(error))
            return
        }

        URLSession.shared.dataTask(with: request) { data, response, error in
            if let error = error {
                completion(.failure(error))
                return
            }

            guard let httpResponse = response as? HTTPURLResponse else {
                completion(.failure(NetworkError.noData))
                return
            }

            switch httpResponse.statusCode {
            case 200:
                do {
                    let decoder = JSONDecoder()
                    let userDefaults = UserDefaults.standard
                    let user = try decoder.decode(User.self, from: data!)
                    completion(.success(user))
                    userDefaults.setValue(user.id, forKey:"user_id")
                    userDefaults.setValue(true, forKey:"login_status")
                } catch {
                    completion(.failure(error))
                }
            case 401:
                completion(.failure(AuthError.wrongPassword))
            case 404:
                completion(.failure(AuthError.userNotFound))
            default:
                completion(.failure(NetworkError.unknownError))
            }
        }.resume()
    }
}

enum AuthError: Error {
    case wrongPassword
    case userNotFound
}

enum NetworkError: Error {
    case invalidURL
    case noData
    case unknownError
}
