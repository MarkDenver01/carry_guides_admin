package com.nathaniel.carryapp.presentation.ui.compose.dashboard.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nathaniel.carryapp.presentation.utils.responsiveDp
import com.nathaniel.carryapp.presentation.utils.responsiveSp

@Composable
fun ServiceOptionCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    val iconSize = responsiveDp(48f)
    val titleSize = responsiveSp(16f)
    val subtitleSize = responsiveSp(13f)
    val cardShape = RoundedCornerShape(24.dp)
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        shape = cardShape,
        color = Color(0xFFE6FF00),
        tonalElevation = 0.dp,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Let Material3 provide default ripple
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier.padding(responsiveDp(16f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    fontSize = titleSize
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF2E7D32),
                    fontSize = subtitleSize
                )
            }

            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "$title Icon",
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
