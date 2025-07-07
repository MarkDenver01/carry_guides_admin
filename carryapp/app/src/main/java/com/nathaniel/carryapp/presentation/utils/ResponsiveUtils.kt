package com.nathaniel.carryapp.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun screenHeightFractionWithLimits(
    fraction: Float,
    min: Dp = 100.dp,
    max: Dp = 300.dp
): Dp {
    val config = LocalConfiguration.current
    val calculated = (config.screenHeightDp * fraction).dp
    return maxOf(min, minOf(calculated, max))
}


@Composable
fun screenWidthFractionWithLimits(
    fraction: Float,
    min: Dp = 80.dp,
    max: Dp = 300.dp
): Dp {
    val config = LocalConfiguration.current
    val calculated = (config.screenWidthDp * fraction).dp
    return maxOf(min, minOf(calculated, max))
}

@Composable
fun responsiveTextSize(
    base: Float = 16f,
    scaleFactor: Float = 0.05f
): TextUnit {
    val config = LocalConfiguration.current
    val scaled = base + (config.screenWidthDp * scaleFactor)
    return scaled.sp
}

@Composable
fun responsiveDp(base: Float): Dp {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val factor = screenWidth / 360f // base on 360dp screen
    return (base * factor).dp
}

@Composable
fun responsiveSp(base: Float): TextUnit {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val factor = screenWidth / 360f
    return (base * factor).sp
}