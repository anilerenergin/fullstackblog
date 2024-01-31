import Foundation
import Combine

class PostViewModel: ObservableObject {
    @Published var posts: [Post] = []
    @Published var isLoading = false // Track loading state
    private var cancellables: Set<AnyCancellable> = []

    private let postRepository: PostRepository

    init(postRepository: PostRepository = .shared) {
        self.postRepository = postRepository
        setupBindings()
        postRepository.getPosts()
    }

    private func setupBindings() {
        postRepository.$posts
            .assign(to: \.posts, on: self)
            .store(in: &cancellables)
    }

    func refreshPosts() {
        postRepository.getPosts()
    }

    func addPost(newPost: PostRequestModel) async throws {
        isLoading = true // Set loading state to true

        do {
            let addedPost = try await postRepository.addPost(newPost: newPost)
            isLoading = false // Set loading state to false once the operation is completed
            // Handle the added post if needed
            // For example, you might want to update your local posts array
            // self.posts.append(addedPost)
        } catch {
            isLoading = false // Set loading state to false in case of an error
            throw error
        }
    }


    func deletePost(postID: Int, completion: @escaping (Result<Void, Error>) -> Void) {
        isLoading = true // Set loading state to true

        postRepository.deletePost(postID: postID) { [self] result in
            isLoading = false // Set loading state to false once the operation is completed

            completion(result.map { _ in () })
        }
    }
}
