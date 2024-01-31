import Foundation
import Combine


class PostRepository: ObservableObject {
    
    static let shared = PostRepository()

    private init() {}

    @Published var posts: [Post] = []

    func getPosts() {
        guard let url = URL(string: "\(AppConstants.baseURL)/api/Post") else { return }

        URLSession.shared.dataTaskPublisher(for: url)
            .map(\.data)
            .decode(type: [Post].self, decoder: JSONDecoder())
            .receive(on: DispatchQueue.main)
            .sink { completion in
                if case .failure(let error) = completion {
                    print("Error fetching posts: \(error)")
                }
            } receiveValue: { [weak self] posts in
                self?.posts = posts
            }
            .store(in: &cancellables)
    }

    func addPost(newPost: PostRequestModel) async throws -> Post {
        guard let url = URL(string: "\(AppConstants.baseURL)/api/Post/add") else {
            throw NSError(domain: "Invalid URL", code: 0, userInfo: nil)
        }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")

        do {
            request.httpBody = try JSONEncoder().encode(newPost)
        } catch {
            throw error
        }

        let (data, _) = try await URLSession.shared.data(for: request)
        return try JSONDecoder().decode(Post.self, from: data)
    }



    func deletePost(postID: Int, completion: @escaping (Result<Void, Error>) -> Void) {
        guard let url = URL(string: "\(AppConstants.baseURL)/api/Post/\(postID)") else {
            completion(.failure(NSError(domain: "Invalid URL", code: 0, userInfo: nil)))
            return
        }

        var request = URLRequest(url: url)
        request.httpMethod = "DELETE"

        URLSession.shared.dataTaskPublisher(for: request)
            .tryMap { data, response in
                guard let httpResponse = response as? HTTPURLResponse,
                      httpResponse.statusCode == 200 else {
                    throw URLError(.badServerResponse)
                }
                return data
            }
            .sink(receiveCompletion: { result in
                switch result {
                case .failure(let error):
                    print("Error deleting post: \(error)")
                    completion(.failure(error))
                case .finished:
                    break // Do nothing on successful completion
                }
            }, receiveValue: { _ in
                // If you need to perform any action on successful deletion
                completion(.success(()))
            })
            .store(in: &cancellables)
    }


    private var cancellables: Set<AnyCancellable> = []
}
