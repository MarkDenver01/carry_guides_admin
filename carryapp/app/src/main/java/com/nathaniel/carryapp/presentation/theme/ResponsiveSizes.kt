package com.nathaniel.carryapp.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ResponsiveSizes(
    val logoSize: Dp,
    val buttonHeight: Dp,
    val buttonWidth: Dp,
    val titleFontSize: TextUnit,
    val buttonFontSize: TextUnit
)

// Default fallback
val LocalResponsiveSizes = staticCompositionLocalOf {
    ResponsiveSizes(
        logoSize = 200.dp,
        buttonHeight = 64.dp,
        buttonWidth = 170.dp,
        titleFontSize = 24.sp,
        buttonFontSize = 16.sp
    )
}