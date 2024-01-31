import Foundation

struct Post: Codable, Identifiable {
    let id: UUID
    let composerName: String
    let title: String
    let postMessage: String
    let dateTime: Date
}
