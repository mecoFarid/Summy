//
//  GameResultDialog.swift
//  iosApp
//
//  Created by Farid Mammadov on 23.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct GameResultDialog: View {
    
    private let gameResult: GameViewModel.ScreenStateCompleted
    private let onRestartGame: () -> Void
    
    init(
        _ gameResult: GameViewModel.ScreenStateCompleted,
        onRestartGame: @escaping () -> Void
    ) {
        self.gameResult = gameResult
        self.onRestartGame = onRestartGame
    }
    
    var body: some View {
        let screenState = gameResult.toScreenState()
        let iconSize = Dimens.gu_12
        VStack{
            Image(screenState.icon)
                .resizable()
                .frame(width: iconSize, height: iconSize)
                .offset(y: iconSize/2.0)
                .zIndex(1)
            
            VStack{
                Text(LocalizedStringKey(screenState.message))
                    .font(.subheadline)
                    .padding([.top, .bottom])
                    .foregroundColor(Colors.priparyTextColor)
                Text("alert_button_restart")
                    .font(.body.bold())
                    .padding([.top, .bottom], Dimens.gu)
                    .frame(maxWidth: .infinity)
                    .background(screenState.buttonColor)
                    .clipShape(RoundedRectangle(cornerRadius: .infinity))
                    .foregroundColor(screenState.buttonTextColor)
                    .onTapGesture(perform: onRestartGame)
            }
            .padding([.top], iconSize / 2.0)
            .padding([.bottom], Dimens.gu_2)
            .padding([.leading, .trailing], Dimens.gu_2)
            .background(Colors.gameplayResultDialog)
            .clipShape(RoundedRectangle(cornerRadius: Dimens.gu_2))
            .padding([.leading, .trailing], Dimens.gu_4)
        }
    }
}

 fileprivate extension GameViewModel.ScreenStateCompleted{
    
    func toScreenState() -> ScreenState{
        switch self {
        case _ as GameViewModel.ScreenStateCompletedFailed:
            return ScreenState(
                icon: "FailureIcon",
                message: "alert_failure",
                buttonColor: Colors.error,
                buttonTextColor: Colors.onError
            )
        case _ as GameViewModel.ScreenStateCompletedSucceeded:
            return ScreenState(
                icon: "SuccessIcon",
                message: "alert_success",
                buttonColor: Colors.success,
                buttonTextColor: Colors.onSuccess
            )
        default: fatalError()
        }
    }
}

fileprivate struct ScreenState{
    let icon: String
    let message: String
    let buttonColor: Color
    let buttonTextColor: Color
}

struct GameResultDialog_Previews: PreviewProvider {
    static var previews: some View {
        GameResultDialog(
            GameViewModel.ScreenStateCompletedSucceeded(moveCount: 2, elapsedTime: 23)
        ){}
    }
}
