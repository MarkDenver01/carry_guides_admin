package com.nathaniel.carryapp.presentation.utils

import androidx.compose.ui.graphics.Color

fun Color.darken(factor: Float = 0.85f): Color {
    return Color(
        red = red * factor,
        green = green * factor,
        blue = blue * factor,
        alpha = alpha
    )
}