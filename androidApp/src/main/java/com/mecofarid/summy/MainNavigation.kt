package com.mecofarid.summy

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mecofarid.summy.screens.game.GAME_GRAPH
import com.mecofarid.summy.screens.game.gameNavigation

@Composable
fun MainNavigation() {
    NavHost(
        navController = rememberNavController(),
        startDestination = GAME_GRAPH
    ) {
        gameNavigation()
    }
}