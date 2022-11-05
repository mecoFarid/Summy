package com.mecofarid.summy.di

import com.mecofarid.summy.features.game.domain.interactor.GetAddendsSumMultiplierInteractor
import com.mecofarid.summy.features.game.domain.interactor.GetAddendsInteractor
import com.mecofarid.summy.features.game.domain.interactor.GetGameCompletionResultInteractor
import com.mecofarid.summy.features.game.domain.interactor.GetTargetNumberInteractor
import com.mecofarid.summy.screens.main.GameViewModel

interface ViewModelComponent {
  val gameViewModel: GameViewModel
}

class ViewModelModule: ViewModelComponent {
  override val gameViewModel: GameViewModel = GameViewModel(
    GetAddendsInteractor(),
    GetTargetNumberInteractor(GetAddendsSumMultiplierInteractor()),
    GetGameCompletionResultInteractor(),
  )
}