//
//  GamePad.swift
//  iosApp
//
//  Created by Farid Mammadov on 23.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct GamePad<PadShape: Shape, Modifier: ViewModifier>: View {

    private let modifier: Modifier
    private let text: Int
    private let shape: PadShape

    init(modifier: Modifier, text: Int, shape: PadShape) {
        self.modifier = modifier
        self.text = text
        self.shape = shape
    }

    var body: some View {
        Text(String(text))
            .modifier(modifier)
            .foregroundColor(Colors.gameplayOnPad)
            .background(Colors.gameplayPad)
            .clipShape(shape)
    }
}

struct SumGamePad: View {
    
    private let text: Int
    
    init(_ text: Int) {
        self.text = text
    }
    
    var body: some View{
        GamePad(modifier: PadViewModifier(), text: text, shape: Circle())
    }
    
    private struct PadViewModifier: ViewModifier{
        
        func body(content: Content) -> some View {
            content
                .padding(Dimens.gu_6)
                .font(Typography.gameplayBigText)
        }
    }
}

struct AdendGamePad: View {
    
    private let text: Int
    
    init(_ text: Int) {
        self.text = text
    }
    
    var body: some View{
        GamePad(modifier: PadViewModifier(), text: text, shape: Circle())
    }
    
    private struct PadViewModifier: ViewModifier{
        
        func body(content: Content) -> some View {
            content
                .padding(Dimens.gu_2)
                .font(Typography.gameplayMediumText)
        }
    }
}

struct TargetGamePad: View {
    
    private let text: Int
    
    init(_ text: Int) {
        self.text = text
    }
    
    var body: some View{
        GamePad(modifier: PadViewModifier(), text: text, shape: RoundedRectangle(cornerRadius: Dimens.gu_0_5))
    }
    
    private struct PadViewModifier: ViewModifier{
        
        func body(content: Content) -> some View {
            content
                .padding([.leading, .trailing], Dimens.gu_2)
                .padding([.top, .bottom], Dimens.gu_0_5)
                .font(Typography.gameplayMediumText)
        }
    }
}

struct GamePad_Previews: PreviewProvider {
    static var previews: some View {
        VStack{
            SumGamePad(123)
            AdendGamePad(45)
            TargetGamePad(123)
        }
    }
}
