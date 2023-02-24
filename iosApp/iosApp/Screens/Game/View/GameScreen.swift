import SwiftUI
import shared

struct GameScreen: View {
    
    var body: some View {

        Text("")

    }
}


struct GameScreenContent: View {
    
    private let screenState: GameViewModel.ScreenState
    private let gameplay: Gameplay
    private let gameProgress: GameProgress
    private let elapseTime: Int
    private let onAdd: (Int) -> Void
    private let onRestartGame: () -> Void
    
    init(
        screenState: GameViewModel.ScreenState,
        gameplay: Gameplay,
        gameProgress: GameProgress,
        elapseTime: Int,
        onAdd: @escaping (Int) -> Void,
        onRestartGame: @escaping () -> Void
    ) {
        self.screenState = screenState
        self.gameplay = gameplay
        self.gameProgress = gameProgress
        self.elapseTime = elapseTime
        self.onAdd = onAdd
        self.onRestartGame = onRestartGame
    }

    var body: some View {
        VStack{
            HStack{
                ProgressIndicator(String(elapseTime))
                TargetGamePad(gameplay.target)
                ProgressIndicator(String(gameProgress.moveCounter))
            }
            
            SumGamePad(gameProgress.sum)
            
            HStack{
                ForEach(gameplay.addends, id: \.self){ addend in
                    AddendGamePad(addend.int32Value)
                        .onTapGesture{
                            onAdd(addend.intValue)
                        }
                }
            }
        }
    }
}

struct ProgressIndicator: View{
    
    private let text: String
    
    init(_ text: String) {
        self.text = text
    }
    
    var body: some View{
        Text(text)
            .foregroundColor(Colors.gameplayIndicatorText)
            .font(Typography.gameplayMediumText)
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
        GameScreenContent(
            screenState: GameViewModel.ScreenStateCompletedFailed(moveCount: 23, elapsedTime: 344),
            gameplay: Gameplay(addends: [KotlinInt(value: 23), KotlinInt(value: 1), KotlinInt(value: 3)], target: 345),
            gameProgress: GameProgress(moveCounter: 23, sum: 145),
            elapseTime: 233,
            onAdd: {_ in },
            onRestartGame: {}
        )
	}
}
