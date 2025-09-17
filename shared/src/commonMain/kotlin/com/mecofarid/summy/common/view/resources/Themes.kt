package com.mecofarid.summy.common.view.resources

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit


@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAppColorScheme provides AppColorScheme,
        LocalAppDimenScheme provides AppDimenScheme
    ) {
        MaterialTheme(
            colorScheme = MaterialColorScheme,
            content = {
                content()
            }
        )
    }
}

object AppTheme {
    val colorScheme: AppColors
        @Composable
        get() = LocalAppColorScheme.current

    val dimens: AppDimens
        @Composable
        get() = LocalAppDimenScheme.current
}

val MaterialColorScheme = lightColorScheme(
    surface = Colors.DarkBlue,
    error = Colors.Red,
    onError = Color.White
)

val LocalAppColorScheme = staticCompositionLocalOf<AppColors> {
    error("Not implemented")
}

val AppColorScheme = AppColors(
    gameplayPad = Colors.Orange,
    gameplayOnPad = Color.White,
    gameplayIndicatorText = Color.White,
    gameplayResultDialog = Color.White,
    primaryTextColor = Color.DarkGray,
    success = Colors.Green,
    onSuccess = Color.White
)

data class AppColors(
    val gameplayPad: Color,
    val gameplayOnPad: Color,
    val gameplayIndicatorText: Color,
    val gameplayResultDialog: Color,
    val primaryTextColor: Color,
    val success: Color,
    val onSuccess: Color
)

val LocalAppDimenScheme = staticCompositionLocalOf<AppDimens> {
    error("Not implemented")
}

val AppDimenScheme = AppDimens(
    gameplayBigText = Dimens.text_gu_4,
    gameplayMediumText = Dimens.text_gu_3
)

data class AppDimens(
    val gameplayBigText: TextUnit,
    val gameplayMediumText: TextUnit,
)
