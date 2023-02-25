package com.mecofarid.summy.app

internal class PlatformApp {
    fun appModule(): AppComponent = AppModule()
}

internal val appComponent by lazy {
    PlatformApp().appModule()
}

