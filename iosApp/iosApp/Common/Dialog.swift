//
//  Dialog.swift
//  iosApp
//
//  Created by Farid Mammadov on 23.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct Dialog<DialogContent: View>: ViewModifier {
    
    private let dialogContent: () -> DialogContent
    private let isDialogShowing: Bool
    
    init(isDialogShowing: Bool, dialogContent: @escaping () -> DialogContent) {
        self.dialogContent = dialogContent
        self.isDialogShowing = isDialogShowing
    }
    
    func body(content: Content) -> some View {
        ZStack{
            content
            if isDialogShowing{
                Rectangle()
                    .edgesIgnoringSafeArea(.all)
                    .foregroundColor(Color.black.opacity(0.6))
                
                dialogContent()
            }
        }
    }
}

extension View {
    func dialog<DialogContent: View>(
        isDialogShowing: Bool,
        dialogContent: @escaping () -> DialogContent
    ) -> some View {
        self.modifier(Dialog(isDialogShowing: isDialogShowing, dialogContent: dialogContent))
    }
}

struct Dialog_Previews: PreviewProvider {
    static var previews: some View {
        Text("TextTextTextTextText \n TextTextTextTextText")
            .background(Color.red)
            .dialog(isDialogShowing: true){
                Text("New text").background(Color.white)
            }
    }
}
