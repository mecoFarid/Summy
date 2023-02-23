package com.mecofarid.summy.screens.game.view

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.mecofarid.summy.R
import com.mecofarid.summy.common.ui.SystemUiPreview
import com.mecofarid.summy.features.game.presentation.GameViewModel
import com.mecofarid.summy.resources.AppTheme
import com.mecofarid.summy.resources.Dimens

@Composable
fun GameResultDialog(
    onRestartGame: () -> Unit,
    gameResult: GameViewModel.ScreenState.Completed
) {
    Dialog(
        onDismissRequest = onRestartGame,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        ConstraintLayout{
            val iconSize = Dimens.gu_12
            val (background) = createRefs()
            Box(
                modifier = Modifier
                    .padding(top = iconSize / 2)
                    .clip(RoundedCornerShape(size = Dimens.gu_4))
                    .background(AppTheme.colorScheme.gameplayResultDialog)
                    .constrainAs(background) {
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom,
                            start = parent.start,
                            end = parent.end
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.gu_2),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val screenState = gameResult.getScreenState()
                Image(
                    modifier = Modifier
                        .size(iconSize),
                    painter = painterResource(id = screenState.icon),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(
                        vertical = Dimens.gu_2
                    ),
                    text = stringResource(id = screenState.message),
                    style = MaterialTheme.typography.titleSmall
                )
                Button(
                    modifier = Modifier
                        .padding(horizontal = Dimens.gu_2)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = screenState.buttonColor),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = Dimens.gu_0_5),
                    onClick = onRestartGame
                ) {
                    Text(
                        text = stringResource(id = R.string.alert_button_restart).uppercase(),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

            }
        }
    }
}

@Composable
private fun GameViewModel.ScreenState.Completed.getScreenState():ScreenState {
    return when (this) {
        is GameViewModel.ScreenState.Completed.Failed ->
            ScreenState(
                R.drawable.failure,
                R.string.alert_failure,
                buttonColor = MaterialTheme.colorScheme.error
            )
        is GameViewModel.ScreenState.Completed.Succeeded ->
            ScreenState(
                R.drawable.success,
                R.string.alert_success,
                buttonColor = AppTheme.colorScheme.success
            )
    }
}

data class ScreenState(
    @DrawableRes val icon: Int,
    @StringRes val message: Int,
    val buttonColor: Color
)

@SystemUiPreview
@Composable
fun PreviewGameResultDialog() {
    AppTheme {
        GameResultDialog(
            onRestartGame = { /*TODO*/ },
            gameResult = GameViewModel.ScreenState.Completed.Succeeded(0, 0)
        )
    }
}
