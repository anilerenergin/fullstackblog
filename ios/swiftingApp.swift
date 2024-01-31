import SwiftUI
import SwiftData

@main
struct blogApp: App {
    var sharedModelContainer: ModelContainer = {
        let schema = Schema([
    
        ])
        let modelConfiguration = ModelConfiguration(schema: schema, isStoredInMemoryOnly: false)

        do {
            return try ModelContainer(for: schema, configurations: [modelConfiguration])
        } catch {
            fatalError("Could not create ModelContainer: \(error)")
        }
    }()

    var body: some Scene {
        let userDefaults = UserDefaults.standard
        let loginStatus = userDefaults.bool(forKey: "login_status")
        WindowGroup {
            if loginStatus {
                        FeedView()
                    } else {
                        LoginView()
                    }
        }
        .modelContainer(sharedModelContainer)
    }
}
