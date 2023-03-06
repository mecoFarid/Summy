package com.mecofarid.summy

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mecofarid.summy.screens.game.GAME_GRAPH
import com.mecofarid.summy.screens.game.gameNavigation

@Composable
fun MainNavigation() {
    val urlHandler = LocalUriHandler.current
    NavHost(
        navController = rememberNavController(),
        startDestination = GAME_GRAPH
    ) {
        gameNavigation {
            urlHandler.openUri(it)
        }
    }
}