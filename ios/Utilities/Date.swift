import Foundation



class DateUtility {
    static func timePassed(from dateString: String) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")

        if let date = dateFormatter.date(from: dateString) {
            let timePassedFormatter = DateComponentsFormatter()
            timePassedFormatter.unitsStyle = .abbreviated

            if let timePassedString = timePassedFormatter.string(from: date, to: Date()) {
                let components = Calendar.current.dateComponents([.second], from: date, to: Date())
                if let seconds = components.second, seconds < 60 {
                    return "Just now"
                } else {
                    return "\(timePassedString) ago"
                }
            }
        }

        return "Invalid date format."
    }
}
