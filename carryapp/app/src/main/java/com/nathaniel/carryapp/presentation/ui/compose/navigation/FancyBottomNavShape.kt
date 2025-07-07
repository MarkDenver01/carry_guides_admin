package com.nathaniel.carryapp.presentation.ui.compose.navigation

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.graphics.Shape

fun fancyBottomNavShape(cutoutRadius: Float = 36f): Shape {
    return GenericShape { size, _ ->
        val width = size.width
        val height = size.height
        val center = width / 2f
        val cutoutWidth = cutoutRadius * 2 + 32f
        val cutoutDepth = cutoutRadius + 16f

        moveTo(0f, 0f)
        lineTo(center - cutoutWidth / 2f, 0f)

        // Curve up
        cubicTo(
            center - cutoutWidth / 2f + cutoutRadius / 2f, 0f,
            center - cutoutRadius, cutoutDepth,
            center, cutoutDepth
        )
        cubicTo(
            center + cutoutRadius, cutoutDepth,
            center + cutoutWidth / 2f - cutoutRadius / 2f, 0f,
            center + cutoutWidth / 2f, 0f
        )

        lineTo(width, 0f)
        lineTo(width, height)
        lineTo(0f, height)
        close()
    }
}