import SwiftUI

struct FeedView: View {
    @ObservedObject var viewModel = PostViewModel()
    let dateUtility = DateUtility()
    
    var body: some View {
        NavigationView {
            List {
                ForEach(Array(viewModel.posts.enumerated()), id: \.element.id) { (index, post) in
                    VStack(alignment: .leading) {
                        HStack {
                            Text(post.title)
                                .font(.headline)
                            Spacer()
                            Button("delete") {
                                viewModel.deletePost(postID: post.id!) { result in
                                    DispatchQueue.main.async {
                                        switch result {
                                        case .success:
                                            print("Post deleted successfully")
                                            if let index = viewModel.posts.firstIndex(where: { $0.id == post.id }) {
                                                viewModel.posts.remove(at: index)
                                            }
                                            
                                        case .failure(let error):
                                            print("Failed to delete post: \(error.localizedDescription)")
                                        }
                                    }
                                }
                            }
                            .foregroundColor(.red)
                            .font(.custom("Satoshi", size: 15))
                            .buttonStyle(.borderless)
                        }
                        
                        Text("By: \(post.composerName)")
                            .font(.subheadline)
                        
                        HStack {
                            Text(post.postMessage)
                                .font(.body)
                                .foregroundColor(.gray)
                            Spacer()
                            Text((DateUtility.timePassed(from: post.dateTime)))
                                .font(.body)
                                .foregroundColor(.gray)
                         

                        }
                    
                    }
                    if index != viewModel.posts.count - 1 {
                        Divider()
                    }

                }
                .navigationBarTitle("Posts")
                .navigationBarItems(trailing: NavigationLink(destination: SharePostView(viewModel: viewModel)) {
                    Image(systemName: "plus")
                })
                .navigationBarBackButtonHidden(true)
                .onAppear {
                    viewModel.refreshPosts()
                }
            }
            .onReceive(viewModel.$posts) { _ in
                // Perform UI update when posts change
            }
        }
    }
    
}

struct FeedView_Previews: PreviewProvider {
    static var previews: some View {
        FeedView()
    }
}
