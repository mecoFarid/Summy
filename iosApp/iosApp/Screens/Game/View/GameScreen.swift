import SwiftUI
import shared

struct GameScreen: View {

    @ObservedObject var viewmodel: ViewModel
    
    var body: some View{
        
        GameScreenContent(
            screenState: viewmodel.screenState,
            gameplay: viewmodel.gameplay,
            gameProgress: viewmodel.gameProgress,
            timeTicker: viewmodel.timeTicker,
            onAdd: { addend in
                viewmodel.gameViewmodel.onAdd(addend: addend)
            },
            onRestartGame: {
                viewmodel.gameViewmodel.restartGame()
            }
        )
    }
    
    class ViewModel: ObservableObject{
        
        private(set) var gameViewmodel: GameViewModel
        @Published private(set) var screenState: GameViewModel.ScreenState
        @Published private(set) var gameplay: Gameplay
        @Published private(set) var gameProgress: GameProgress
        @Published private(set) var timeTicker: Int
        
        init(_ gameViewmodel: GameViewModel) {
            self.gameViewmodel = gameViewmodel
            self.screenState = gameViewmodel.screenState.value as! GameViewModel.ScreenState
            self.gameplay = gameViewmodel.gameplay.value as! Gameplay
            self.gameProgress = gameViewmodel.gameProgress.value as! GameProgress
            self.timeTicker = gameViewmodel.timeTicker.value as! Int
            
            self.startObservers()
        }
        
        private func startObservers(){
            gameViewmodel.observe(flow: gameViewmodel.screenState){ state in
                self.screenState = state as! GameViewModel.ScreenState
            }
            gameViewmodel.observe(flow: gameViewmodel.gameplay){ gameplay in
                self.gameplay = gameplay as! Gameplay
            }
            gameViewmodel.observe(flow: gameViewmodel.gameProgress){ gameProgress in
                self.gameProgress = gameProgress as! GameProgress
            }
            gameViewmodel.observe(flow: gameViewmodel.timeTicker){ timeTicker in
                self.timeTicker = timeTicker as! Int
            }
        }
        
        deinit{
            gameViewmodel.clear()
        }
    }
    
}

struct GameScreenContent: View {
    
    private let screenState: GameViewModel.ScreenState
    private let gameplay: Gameplay
    private let gameProgress: GameProgress
    private let timeTicker: Int
    private let onAdd: (Int32) -> Void
    private let onRestartGame: () -> Void
    
    init(
        screenState: GameViewModel.ScreenState,
        gameplay: Gameplay,
        gameProgress: GameProgress,
        timeTicker: Int,
        onAdd: @escaping (Int32) -> Void,
        onRestartGame: @escaping () -> Void
    ) {
        self.screenState = screenState
        self.gameplay = gameplay
        self.gameProgress = gameProgress
        self.timeTicker = timeTicker
        self.onAdd = onAdd
        self.onRestartGame = onRestartGame
    }
    
    var body: some View {
        VStack{
            HStack{
                ProgressIndicator(
                    TimeUtilsKt.formatTime(
                        Int64(timeTicker),
                        pattern: TimeUtilsKt.MINUTE_SECOND_PATTERN,
                        locale: Locale.current
                    )
                ).frame(maxWidth: .infinity, alignment: Alignment.leading)
                
                TargetGamePad(gameplay.target)
                    .frame(maxWidth: .infinity)
                
                ProgressIndicator(String(gameProgress.moveCounter))
                    .frame(maxWidth: .infinity, alignment: Alignment.trailing)
            }
            
            Spacer()
            
            SumGamePad(text: Binding(get: {gameProgress.sum}, set: {_ in}))
            
            Spacer()
            
            HStack{
                ForEach(gameplay.addends, id: \.self){ value in
                    let addend = value.int32Value
                    AddendGamePad(addend){
                        onAdd(addend)
                    }
                    .padding([.leading, .trailing], Dimens.gu_2)
                }
            }
            .padding(.bottom, Dimens.gu_6)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding()
        .background(Colors.background.ignoresSafeArea())
        .dialog(isDialogShowing: screenState is GameViewModel.ScreenStateCompleted){
            GameResultDialog(screenState as! GameViewModel.ScreenStateCompleted){
                onRestartGame()
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
            timeTicker: 233,
            onAdd: {_ in },
            onRestartGame: {}
        )
	}
}
