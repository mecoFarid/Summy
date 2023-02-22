package com.mecofarid.summy.screens.game

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.mecofarid.summy.features.game.presentation.GameViewModel

private const val GAME = "game"
const val GAME_GRAPH = "game_graph"

fun NavGraphBuilder.gameNavigation() {
    navigation(startDestination = GAME, route = GAME_GRAPH) {
        composable(route = GAME) {
            GameScreen(
                gameViewModel = viewModel(
                    factory = viewModelFactory {
                        initializer {
                            GameViewModel.Factory()
                        }
                    }
                )
            )
        }
    }
}