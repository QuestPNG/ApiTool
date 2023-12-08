package com.beck.apitool.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary =               frappe_lavender,
    onPrimary =             frappe_mantle,
    primaryContainer =      frappe_base,
    onPrimaryContainer =    frappe_text,
    secondary =             frappe_peach,
    onSecondary =           frappe_mantle,
    tertiary =              frappe_blue,
    background =            frappe_mantle,
    surface =               frappe_crust,
    onSurface =             frappe_text,
    surfaceVariant =        frappe_mantle,
    secondaryContainer =    frappe_mantle,
    onBackground =          frappe_text,

    error = frappe_red,
)

private val LightColorScheme = lightColorScheme(
    primary =               latte_lavender,
    onPrimary =             latte_mantle,
    primaryContainer =      latte_base,
    onPrimaryContainer =    latte_text,
    secondary =             latte_peach,
    onSecondary =           latte_mantle,
    tertiary =              latte_blue,
    background =            latte_mantle,
    surface =               latte_crust,
    onSurface =             latte_text,
    surfaceVariant =        latte_mantle,
    secondaryContainer =    latte_mantle,
    onBackground =          latte_text,

    error =                 latte_red,
)
/*private val LightColorScheme = lightColorScheme(
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
)*/

@Composable
fun ApiToolTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    //val colorScheme = when {
    //    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
    //        val context = LocalContext.current
    //        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    //    }

    //    darkTheme -> DarkColorScheme
    //    else -> LightColorScheme
    //}
    val colorScheme = if (darkTheme)  DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}