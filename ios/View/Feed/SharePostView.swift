import SwiftUI

struct SharePostView: View {
    @ObservedObject var viewModel: PostViewModel
    @State private var newName = ""
    @State private var newPostText = ""
    @State private var newPostTitle = ""
    @Environment(\.presentationMode) var presentationMode
    var body: some View {
        VStack { TextField("Enter your name", text: $newName)
                .padding()
                .textFieldStyle(RoundedBorderTextFieldStyle())
            TextField("Enter your title", text: $newPostTitle)
                .padding()
                .textFieldStyle(RoundedBorderTextFieldStyle())
            TextField("Enter your post", text: $newPostText)
                .padding()
                .textFieldStyle(RoundedBorderTextFieldStyle())
            
            Button("Share Post") {
                Task {
                    do {
                        let dateFormatter = DateFormatter()
                        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
                        dateFormatter.timeZone = TimeZone(abbreviation: "UTC") // Set the time zone to UTC if needed

                        let currentDate = Date()
                        let dateString = dateFormatter.string(from: currentDate)
                        print(dateString)
                        let newPost = PostRequestModel(composerName: newName, title: newPostTitle, postMessage: newPostText, dateTime: dateString)
                        try await viewModel.addPost(newPost: newPost)
                        print("Post shared successfully")
                        print("Must dismiss by time")
                        presentationMode.wrappedValue.dismiss()
                    } catch {
                        print("Error sharing post: \(error.localizedDescription)")
                    }
                }
            }
            
            .padding()
            .foregroundColor(.white)
            .background(Color.blue)
            .cornerRadius(8)
            .padding()
            
            Spacer()
        }
        .padding()
        .navigationBarTitle("Share Post", displayMode: .inline)
    }
}

struct SharePostView_Previews: PreviewProvider {
    static var previews: some View {
        SharePostView(viewModel: PostViewModel())
    }
}
