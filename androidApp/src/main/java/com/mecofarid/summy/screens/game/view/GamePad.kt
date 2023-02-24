package com.mecofarid.summy.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import com.mecofarid.summy.common.ui.SystemUiPreview
import com.mecofarid.summy.common.ui.ext.bounce
import com.mecofarid.summy.common.ui.ext.clickableWithoutIndication
import com.mecofarid.summy.common.ui.ext.squareAspectRatio
import com.mecofarid.summy.resources.AppTheme
import com.mecofarid.summy.resources.Dimens

@Composable
private fun GamePad(
    modifier: Modifier = Modifier,
    shape: Shape,
    text: Int,
    fontSize: TextUnit
) {
    Box(
        Modifier
            .clip(shape)
            .background(AppTheme.colorScheme.gameplayPad)
            .then(modifier)
    ) {
        Text(
            text = text.toString(),
            style = TextStyle(
                color = AppTheme.colorScheme.gameplayOnPad,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun CircleGamePad(
    modifier: Modifier = Modifier,
    text: Int,
    fontSize: TextUnit
) {
    GamePad(
        shape = CircleShape,
        modifier = modifier
            .squareAspectRatio(),
        text = text,
        fontSize = fontSize
    )
}

@Composable
fun SumGamePad(
    modifier: Modifier = Modifier,
    text: Int
) {
    Box(
        modifier = modifier
            .bounce(
                bounceWhenChanged = text,
                maxScale = 1.1f
            ),
    ) {
        CircleGamePad(
            modifier = Modifier
                .padding(Dimens.gu_6),
            text = text,
            fontSize = AppTheme.dimens.gameplayBigText
        )
    }
}

@Composable
fun AddendGamePad(
    modifier: Modifier = Modifier,
    text: Int,
    onClick: (Int) -> Unit
) {
    var clicked by remember { mutableStateOf(false) }
    Box(modifier = modifier
        .clickableWithoutIndication {
            clicked = !clicked
            onClick(text)
        }
        .bounce(
            bounceWhenChanged = clicked,
            maxScale = 0.95f
        ),
    ) {
        CircleGamePad(
            modifier = Modifier
                .padding(Dimens.gu_2),
            text = text,
            fontSize = AppTheme.dimens.gameplayMediumText
        )
    }
}

@Composable
fun TargetGamePad(
    modifier: Modifier = Modifier,
    text: Int
) {
    GamePad(
        shape = RoundedCornerShape(size = Dimens.gu_0_5),
        modifier = modifier
            .padding(vertical = Dimens.gu_0_5, horizontal = Dimens.gu_2),
        text = text,
        fontSize = AppTheme.dimens.gameplayMediumText
    )
}

@SystemUiPreview
@Composable
fun Preview_GamePad() {
    AppTheme {
        Column {
            TargetGamePad(text = 123)
            SumGamePad(text = 23)
            AddendGamePad(text = 7, onClick = {})
        }
    }
}