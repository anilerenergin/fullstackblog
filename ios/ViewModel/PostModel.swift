
import Foundation
struct Post: Codable, Identifiable {
    let id: Int?
    let composerName: String
    let title: String
    let postMessage: String
    let dateTime: String 
}
struct PostRequestModel: Codable {
    var composerName: String
    var title: String
    var postMessage: String
    var dateTime: String
}
