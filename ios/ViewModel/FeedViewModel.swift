import Foundation

class PostViewModel {
    private let apiService: APIService

    var posts: [Post] = []

    init(apiService: APIService = APIService()) {
        self.apiService = apiService
    }

    func getPosts(completion: @escaping (Result<[Post], Error>) -> Void) {
        apiService.getPosts { result in
            switch result {
            case .success(let posts):
                self.posts = posts
                completion(.success(posts))
            case .failure(let error):
                completion(.failure(error))
            }
        }
    }

    func addPost(post: Post, completion: @escaping (Result<Post, Error>) -> Void) {
        apiService.addPost(post: post) { result in
            switch result {
            case .success(let newPost):
                self.posts.append(newPost)
                completion(.success(newPost))
            case .failure(let error):
                completion(.failure(error))
            }
        }
    }
}
