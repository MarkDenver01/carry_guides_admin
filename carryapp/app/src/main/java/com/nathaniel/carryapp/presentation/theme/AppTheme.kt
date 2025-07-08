package com.nathaniel.carryapp.presentation.theme


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Define your custom colors
data class AppColors(
    val primary: Color = Color(0xFF4CAF50),
    val primaryDark: Color = Color(0xFF2E7D32),
    val textPrimary: Color = Color.White,
    val textSecondary: Color = Color.LightGray,
    val error: Color = Color.Red
)

val LocalAppColors = staticCompositionLocalOf { AppColors() }

// Define spacing scale
data class AppSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val lsm: Dp = 12.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
    val xxl: Dp = 48.dp
)

val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }

// Define typography
data class AppTypography(
    val titleLarge: TextStyle = TextStyle(fontSize = 22.sp),
    val body: TextStyle = TextStyle(fontSize = 16.sp),
    val caption: TextStyle = TextStyle(fontSize = 12.sp)
)

val LocalAppTypography = staticCompositionLocalOf { AppTypography() }