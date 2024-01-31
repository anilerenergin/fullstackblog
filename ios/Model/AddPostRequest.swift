// PostRequest.swift
import Foundation

struct PostRequest: Codable {
    var composerName: String
    var title: String
    var postMessage: String
    var dateTime: Date
}
