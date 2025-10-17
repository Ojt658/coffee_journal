package com.dev.coffeejournal.ui.theme

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

/**
 * The default dark color scheme for the application.
 */
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

/**
 * The default light color scheme for the application.
 */
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

/**
 * The light color scheme for the coffee theme.
 */
private val LightCoffeeTheme = lightColorScheme(
    primary = Espresso,
    secondary = Coffee,
    tertiary = Almond,
    primaryContainer = Caramel,
    background = Caramel
)

/**
 * The dark color scheme for the coffee theme.
 */
private val DarkCoffeeTheme = darkColorScheme(
    primary = Vanilla,
    secondary = Almond,
    tertiary = Caramel,
    primaryContainer = Coffee,
    background = Vanilla
)


/**
 * The main theme for the Coffee Journal application.
 *
 * This theme applies a custom color scheme and typography to the application.
 *
 * @param darkTheme Whether to use the dark theme or not.
 * @param dynamicColor Whether to use dynamic colors (available on Android 12+).
 * @param content The content to be displayed within the theme.
 */
@Composable
fun CoffeeJournalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkCoffeeTheme
        else -> LightCoffeeTheme
    }

    MaterialTheme(
        colorScheme = DarkCoffeeTheme,
        typography = Typography,
        content = content
    )
}