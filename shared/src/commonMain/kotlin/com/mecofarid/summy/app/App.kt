package com.mecofarid.summy.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mecofarid.summy.common.view.resources.AppTheme
import com.mecofarid.summy.features.game.presentation.GameViewModel
import com.mecofarid.summy.features.game.view.GameScreen
import com.mecofarid.yarus.ads.google.GoogleAds
import com.mecofarid.yarus.core.platform.PlatformConfig

@Composable
fun App(
    platformConfig: PlatformConfig,
    gameViewModel: GameViewModel = viewModel { GameViewModel.Factory() },
    onHandleUrl: (String) -> Unit
) {
    AppTheme {

        GoogleAds.initialize(platformConfig)

        Scaffold(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .systemBarsPadding()
        ) {
            GameScreen(
                gameViewModel = gameViewModel,
                onHandleUrl = onHandleUrl
            )
        }

    }
}
