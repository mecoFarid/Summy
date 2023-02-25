import SwiftUI
import shared

@main
struct SummyApp: App {
	var body: some Scene {
		WindowGroup {
            GameScreen(viewmodel: GameScreen.ViewModel(GameViewModel.Companion.shared.Factory()))
		}
	}
}
