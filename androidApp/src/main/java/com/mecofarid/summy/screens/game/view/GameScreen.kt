package com.mecofarid.summy.screens.game.view

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mecofarid.summy.R
import com.mecofarid.summy.common.ui.SystemUiPreview
import com.mecofarid.summy.common.utils.formatTime
import com.mecofarid.summy.features.game.domain.model.GameProgress
import com.mecofarid.summy.features.game.domain.model.Gameplay
import com.mecofarid.summy.features.game.presentation.GameViewModel
import com.mecofarid.summy.features.game.utils.MINUTE_SECOND_PATTERN
import com.mecofarid.summy.resources.AppTheme
import com.mecofarid.summy.resources.Dimens
import com.mecofarid.summy.screens.game.AddendGamePad
import com.mecofarid.summy.screens.game.SumGamePad
import com.mecofarid.summy.screens.game.TargetGamePad
import java.util.*

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
    Column {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(Dimens.gu_2)
            ,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                ProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = elapseTime.formatTime(MINUTE_SECOND_PATTERN, Locale.getDefault())
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
            painter = painterResource(id = R.drawable.ic_privacy),
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

@SystemUiPreview
@Composable
fun PreviewGameScreenContent() {
    AppTheme {
        Surface {
            var gameProgress by remember {
                mutableStateOf(GameProgress(459, 78))
            }
            GameScreenContent(
                screenState = GameViewModel.ScreenState.Running,
                gameplay = Gameplay(listOf(1, 24, 3), 105),
                gameProgress = gameProgress,
                elapseTime = 100,
                onAdd = {
                    gameProgress = gameProgress.copy(sum = gameProgress.sum + it)
                },
                onRestartGame = {},
                onHandleUrl = {}
            )
        }
    }
}
