package com.mecofarid.summy.di

interface AppComponent{
  val viewModelComponent: ViewModelComponent
}

class AppModule: AppComponent{
  override val viewModelComponent: ViewModelComponent = ViewModelModule()
}