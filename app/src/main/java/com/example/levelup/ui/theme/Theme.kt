package com.example.levelup.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.levelup.theme.Typography


// ----------- ESQUEMAS DE COLOR PERSONALIZADOS -----------





// ----------- ESQUEMAS DE COLOR PERSONALIZADOS -----------



private val DarkColorScheme = darkColorScheme(

    primary = GamerGreen,

    secondary = GamerBlue,

    background = JetBlack,

    surface = DarkGray,

    onPrimary = JetBlack,

    onSecondary = PureWhite,

    onBackground = PureWhite,

    onSurface = PureWhite,
    error = DangerRed

)

private val LightColorScheme = lightColorScheme(

    primary = GamerGreen,

    secondary = GamerBlue,

    background = PureWhite,

    surface = LightGray,

    onPrimary = PureWhite,

    onSecondary = JetBlack,

    onBackground = JetBlack,

    onSurface = JetBlack,
    error = DangerRed

)



// ----------- FUNCIÓN DE TEMA GLOBAL -----------



@Composable
fun LevelUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),

    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}