//
//  Animation.swift
//  iosApp
//
//  Created by Farid Mammadov on 25.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI


struct BouncingView<AnimationKey: Equatable, Content: View>: View {
    
    @Binding var animationKey: AnimationKey
    @State private var scale = 1.0
    var targetScale: CGFloat
    var scaleRestoreTime: Double
    var content :() -> Content
    
    var body: some View{
        content()
            .scaleEffect(scale)
            .onChange(of: animationKey){ _ in
                withAnimation(Animation.interpolatingSpring(mass: 0.5, stiffness: 3000, damping: 10)
                    ){
                        scale = targetScale
                    }
                    withAnimation(Animation.interpolatingSpring(mass: 1, stiffness: 5000, damping: 1000).delay(scaleRestoreTime)
                    ){
                        scale = 1
                    }
            }
    }
}

struct BouncingViewPreview: View {
    
    @State var scale: Bool = false
    
    var body: some View {
        BouncingView(animationKey: $scale, targetScale: 1.2, scaleRestoreTime: 0.2){
            Text("Hello, world!")
        }.onTapGesture {
            scale.toggle()
        }
    }
}

struct BouncingViewPreview_Previews: PreviewProvider {
    static var previews: some View {
        BouncingViewPreview()
    }
}
