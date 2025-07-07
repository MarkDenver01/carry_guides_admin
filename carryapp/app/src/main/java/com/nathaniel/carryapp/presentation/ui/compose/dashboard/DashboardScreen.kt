package com.nathaniel.carryapp.presentation.ui.compose.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.components.PromoBannerCarousel
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.service.ServiceOptionCard
import com.nathaniel.carryapp.presentation.ui.compose.navigation.BottomNavigationBar
import com.nathaniel.carryapp.presentation.utils.responsiveDp
import com.nathaniel.carryapp.presentation.utils.responsiveSp

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()
    val navigateTo by viewModel.navigateTo.collectAsState()

    val outerPadding = responsiveDp(16f)
    val iconSize = responsiveDp(36f)
    val bannerHeight = responsiveDp(180f)
    val cardPadding = responsiveDp(16f)
    val sectionSpacing = responsiveDp(20f)

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2E7D32), Color(0xFF4CAF50))
                )
            )
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + responsiveDp(16f)), // Reserve space for bottom nav
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = outerPadding, vertical = responsiveDp(12f)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(iconSize)
                )

                Text(
                    text = "Mabuhay, $userName",
                    color = Color.White,
                    fontSize = responsiveSp(17f)
                )

                Image(
                    painter = painterResource(id = R.drawable.logs),
                    contentDescription = "Logo",
                    modifier = Modifier.size(iconSize)
                )
            }

            // Promo Banner
            PromoBannerCarousel(
                modifier = Modifier,
                bannerHeight = bannerHeight
            )
            Spacer(modifier = Modifier.height(sectionSpacing))

            // Service Section Container (Green Pill)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = outerPadding)
                    .background(
                        color = Color(0xFF1B5E20),
                        shape = RoundedCornerShape(responsiveDp(40f))
                    )
                    .padding(cardPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ServiceOptionCard(
                    title = "Delivery",
                    subtitle = "Groceries delivered at your doorsteps",
                    iconRes = R.drawable.delivery_scooter,
                    onClick = { viewModel.onDeliveryClick() }
                )
                Spacer(modifier = Modifier.height(responsiveDp(16f)))
                ServiceOptionCard(
                    title = "In-store Pickup",
                    subtitle = "Pick up at your favorite store",
                    iconRes = R.drawable.cart_basket,
                    onClick = { viewModel.onPickupClick() }
                )
            }

            Spacer(modifier = Modifier.height(sectionSpacing))
        }

        // Bottom Navigation
        BottomNavigationBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
