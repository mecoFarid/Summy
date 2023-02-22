package com.mecofarid.summy.app

import com.mecofarid.summy.features.game.GameComponent
import com.mecofarid.summy.features.game.GameModule

internal interface AppComponent{
    val gameComponent: GameComponent
}

internal class AppModule: AppComponent{
    override val gameComponent: GameComponent by lazy {
        GameModule()
    }
}
