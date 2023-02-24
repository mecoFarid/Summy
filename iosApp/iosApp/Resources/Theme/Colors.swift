//
//  Colors.swift
//  iosApp
//
//  Created by Farid Mammadov on 23.02.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

fileprivate extension Color {
    static let darkBlue = Color(0x3e4e5c)
    static let orange = Color(0xfcba03)
    static let darkOrange = Color(0xa48b2a)
    static let green = Color(0x74c470)
    static let red = Color(0xD03F3F)
}



enum Colors {
    static let background = Color.darkBlue
    static let gameplayPad = Color.orange
    static let gameplayOnPad = Color.white
    static let gameplayIndicatorText = Color.white
    static let gameplayResultDialog = Color.white
    static let success = Color.green
    static let onSuccess = Color.white
    static let error = Color.red
    static let onError = Color.white
}
