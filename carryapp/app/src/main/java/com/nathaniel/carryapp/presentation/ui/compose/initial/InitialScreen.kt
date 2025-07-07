package com.nathaniel.carryapp.presentation.ui.compose.initial

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.navigation.Routes
import com.nathaniel.carryapp.presentation.theme.BgApp
import com.nathaniel.carryapp.presentation.theme.BorderColor
import com.nathaniel.carryapp.presentation.utils.responsiveTextSize
import com.nathaniel.carryapp.presentation.utils.screenHeightFractionWithLimits
import kotlinx.coroutines.delay

@Composable
fun InitialScreen(
    navController: NavController,
    viewModel: InitialViewModel = hiltViewModel()
) {
    // Observe navigation
    LaunchedEffect(Unit) {
        viewModel.navigateToDashboard.collect {
            navController.navigate(Routes.DASHBOARD) {
                popUpTo(Routes.INITIAL) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF4CAF50),Color(0xFF2E7D32))
                )
            )
    ) {
        val logoSize = screenHeightFractionWithLimits(0.45f, min = 180.dp, max = 320.dp)
        val buttonHeight = screenHeightFractionWithLimits(0.065f, min = 56.dp, max = 76.dp)
        val titleFontSize = responsiveTextSize(base = 20f, scaleFactor = 0.035f)
        val buttonFontSize = responsiveTextSize(base = 13f, scaleFactor = 0.03f)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.8f))

            // Title & Logo
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Wrap & Carry",
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.logo_final),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(logoSize)
                        .padding(bottom = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Animated Button
            var pressed by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(
                targetValue = if (pressed) 0.97f else 1f,
                label = "scale"
            )

            Button(
                onClick = {
                    pressed = true
                    viewModel.onGetStartedClicked()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(buttonHeight)
                    .scale(scale),
                border = BorderStroke(1.dp, BorderColor),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = BgApp,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Get Started!",
                        color = Color.White,
                        fontSize = buttonFontSize,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1.2f))

            // Reset animation
            LaunchedEffect(pressed) {
                if (pressed) {
                    delay(120)
                    pressed = false
                }
            }
        }
    }
}
