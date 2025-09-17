package com.mecofarid.summy.features.game.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mecofarid.summy.common.utils.formatTime
import com.mecofarid.summy.features.game.domain.model.GameProgress
import com.mecofarid.summy.features.game.domain.model.Gameplay
import com.mecofarid.summy.features.game.presentation.GameViewModel
import com.mecofarid.summy.features.game.utils.MINUTE_SECOND_PATTERN
import com.mecofarid.summy.common.view.resources.AppTheme
import com.mecofarid.summy.common.view.resources.Dimens
import org.jetbrains.compose.resources.painterResource
import summy.shared.generated.resources.Res
import summy.shared.generated.resources.ic_privacy

private const val URL_PRIVACY_POLICY =
    "http://htmlpreview.github.io/?https://github.com/mecoFarid/Summy/blob/main/legal/privacy_policy.html"

@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    onHandleUrl: (String) -> Unit
) {
    gameViewModel.apply {
        GameScreenContent(
            screenState = screenState.collectAsStateWithLifecycle().value,
            gameplay = gameplay.collectAsStateWithLifecycle().value,
            gameProgress = gameProgress.collectAsStateWithLifecycle().value,
            elapseTime = timeTicker.collectAsStateWithLifecycle().value,
            onAdd = { onAdd(it) },
            onRestartGame = { restartGame() },
            onHandleUrl = onHandleUrl
        )
    }
}

@Composable
fun GameScreenContent(
    screenState: GameViewModel.ScreenState,
    gameplay: Gameplay,
    gameProgress: GameProgress,
    elapseTime: Long,
    onAdd: (Int) -> Unit,
    onRestartGame: () -> Unit,
    onHandleUrl: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(Dimens.gu_2),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                ProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = elapseTime.formatTime(MINUTE_SECOND_PATTERN)
                )
                TargetGamePad(
                    modifier = Modifier.align(Alignment.Center),
                    text = gameplay.target
                )
                ProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = gameProgress.moveCounter.toString()
                )
            }

            SumGamePad(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = gameProgress.sum
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.gu_6),
                horizontalArrangement = Arrangement.spacedBy(Dimens.gu_4, Alignment.CenterHorizontally)
            ) {
                gameplay.addends.forEach {
                    AddendGamePad(
                        text = it,
                        onClick = {
                            onAdd(it)
                        }
                    )
                }
            }

            if (screenState.isGameCompleted())
                GameResultDialog(
                    onRestartGame = onRestartGame,
                    gameResult = screenState as GameViewModel.ScreenState.Completed
                )
        }

        Image(
            modifier = Modifier
                .padding(bottom = Dimens.gu_2)
                .clickable {
                    onHandleUrl(URL_PRIVACY_POLICY)
                }
                .clip(
                    RoundedCornerShape(
                        topEnd = Dimens.gu_14,
                        bottomEnd = Dimens.gu_14
                    )
                )
                .background(AppTheme.colorScheme.gameplayPad)
                .padding(Dimens.gu)
                .size(Dimens.gu_3),
            painter = painterResource(resource = Res.drawable.ic_privacy),
            colorFilter = ColorFilter.tint(AppTheme.colorScheme.gameplayIndicatorText),
            contentDescription = null
        )
    }
}

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            color = AppTheme.colorScheme.gameplayIndicatorText,
            fontSize = AppTheme.dimens.gameplayMediumText
        )
    )
}
