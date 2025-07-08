package com.nathaniel.carryapp.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nathaniel.carryapp.presentation.utils.responsiveTextSize
import com.nathaniel.carryapp.presentation.utils.screenHeightFractionWithLimits
import com.nathaniel.carryapp.presentation.utils.screenWidthFractionWithLimits

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

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

@Composable
fun CarryappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    // Responsive sizes calculation
    val responsiveSizes = ResponsiveSizes(
        logoSize = screenHeightFractionWithLimits(0.45f, min = 180.dp, max = 320.dp),
        buttonHeight = screenHeightFractionWithLimits(0.065f, min = 56.dp, max = 76.dp),
        buttonWidth = screenWidthFractionWithLimits(0.45f, min = 140.dp, max = 220.dp),
        titleFontSize = responsiveTextSize(base = 20f, scaleFactor = 0.035f),
        buttonFontSize = responsiveTextSize(base = 13f, scaleFactor = 0.03f)
    )

    CompositionLocalProvider(
        LocalResponsiveSizes provides responsiveSizes,
        LocalAppColors provides AppColors(), // Custom color set (optional but useful)
        LocalAppSpacing provides AppSpacing(),
        LocalAppTypography provides AppTypography()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}