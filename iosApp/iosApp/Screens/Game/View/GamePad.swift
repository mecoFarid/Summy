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
    private let text: Int32
    private let shape: PadShape

    init(modifier: Modifier, text: Int32, shape: PadShape) {
        self.modifier = modifier
        self.text = text
        self.shape = shape
    }

    var body: some View {
        HStack{
            Text(String(text))
                .modifier(modifier)
                .foregroundColor(Colors.gameplayOnPad)
                .background(Colors.gameplayPad)
                .clipShape(shape)
        }
    }
}

struct SumGamePad: View {
    
    private let text: Int32
    
    init(_ text: Int32) {
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

struct AddendGamePad: View {

    private let text: Int32

    init(_ text: Int32) {
        self.text = text
    }

    var body: some View{
        GamePad(modifier: PadViewModifier(), text: text, shape: Circle())
    }

    private struct PadViewModifier: ViewModifier{
        
        @State private var size: CGSize = .zero
        
        func body(content: Content) -> some View {
            
            content
                .squareAspectRatio(initialSize: $size)
                .padding(Dimens.gu_2)
                .font(Typography.gameplayMediumText)

        }
    }
}

struct TargetGamePad: View {
    
    private let text: Int32
    
    init(_ text: Int32) {
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
        VStack {
            TargetGamePad(4)
            SumGamePad(44)
            AddendGamePad(45)
            AddendGamePad(2)
        }
        
    }
}
