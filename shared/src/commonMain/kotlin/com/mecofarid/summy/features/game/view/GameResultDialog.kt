package com.mecofarid.summy.features.game.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mecofarid.summy.features.game.presentation.GameViewModel
import com.mecofarid.summy.common.view.resources.AppTheme
import com.mecofarid.summy.common.view.resources.Dimens
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import summy.shared.generated.resources.Res
import summy.shared.generated.resources.alert_button_restart
import summy.shared.generated.resources.alert_failure
import summy.shared.generated.resources.alert_success
import summy.shared.generated.resources.failure
import summy.shared.generated.resources.success

@Composable
fun GameResultDialog(
    onRestartGame: () -> Unit,
    gameResult: GameViewModel.ScreenState.Completed
) {
    Dialog(
        onDismissRequest = onRestartGame,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(size = Dimens.gu_4))
                .background(AppTheme.colorScheme.gameplayResultDialog)
                .padding(
                    vertical = Dimens.gu_2,
                    horizontal = Dimens.gu_2,
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Dimens.gu_2),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val screenState = gameResult.getScreenState()
            Image(
                modifier = Modifier
                    .size(Dimens.gu_12),
                painter = painterResource(resource = screenState.icon),
                contentDescription = null
            )
            Text(
                text = stringResource(resource = screenState.message),
                style = MaterialTheme.typography.titleSmall,
                color = AppTheme.colorScheme.primaryTextColor
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = screenState.buttonColor),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = Dimens.gu_0_5),
                onClick = onRestartGame
            ) {
                Text(
                    text = stringResource(resource = Res.string.alert_button_restart),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun GameViewModel.ScreenState.Completed.getScreenState(): ScreenState {
    return when (this) {
        is GameViewModel.ScreenState.Completed.Failed ->
            ScreenState(
                Res.drawable.failure,
                Res.string.alert_failure,
                buttonColor = MaterialTheme.colorScheme.error
            )
        is GameViewModel.ScreenState.Completed.Succeeded ->
            ScreenState(
                Res.drawable.success,
                Res.string.alert_success,
                buttonColor = AppTheme.colorScheme.success
            )
    }
}

data class ScreenState(
    val icon: DrawableResource,
    val message: StringResource,
    val buttonColor: Color
)
