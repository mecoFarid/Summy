//
//  View+Extensions.swift
//  iosApp
//
//  Created by Farid Mammadov on 24.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

extension View{
    func measure(initialSize size: Binding<CGSize>) -> some View{
        self.background(
            GeometryReader { geometry in
                // We want update to be waiting on queue. Otherwise, we chaneg view state while it is being updated
                DispatchQueue.main.async {
                    print("KOK 3")
                    size.wrappedValue = geometry.size
                }
                return Color.clear
            }
        )
    }
}

extension View{
    func squareAspectRatio(initialSize size: Binding<CGSize>) -> some View{
        
        let unwrappedSize = size.wrappedValue
        let frameSize = max(unwrappedSize.width, unwrappedSize.height)
        
        if (frameSize > 0) {
            return AnyView(
                self.frame(
                    width: frameSize,
                    height: frameSize
                ).measure(initialSize: size)
            )
        }
        
        return AnyView(self.measure(initialSize: size))
    }
}

fileprivate struct SsquareAspectRatio: View{
    
    @State var size = CGSize.zero
    var body: some View {
        
        Text("Hello I am some arbitrary text.")
            .foregroundColor(Color.white)
            .squareAspectRatio(initialSize: $size)
            .background(Color.red)
        
    }
}

struct SsquareAspectRatio_Preview: PreviewProvider{
    static var previews: some View{
        SsquareAspectRatio()
    }
}
