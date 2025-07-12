package com.nathaniel.carryapp.presentation.ui.compose.membership.apply

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nathaniel.carryapp.presentation.theme.LocalAppSpacing
import com.nathaniel.carryapp.presentation.theme.LocalResponsiveSizes
import com.nathaniel.carryapp.presentation.ui.compose.membership.MembershipViewModel
import com.nathaniel.carryapp.presentation.ui.compose.navigation.TopNavigationBar
import com.nathaniel.carryapp.presentation.utils.DynamicInfoCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SukiMembershipScreen(
    navController: NavController,
    viewModel: MembershipViewModel = hiltViewModel()
) {
    val navigateTo by viewModel.navigateTo.collectAsState()
    val sizes = LocalResponsiveSizes.current
    val spacing = LocalAppSpacing.current

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(navigateTo) {
        navigateTo?.let { route ->
            navController.navigate(route)
            viewModel.resetNavigation()
        }
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopNavigationBar(
                navController = navController,
                title = "Suki Carry Membership",
                showBackButton = true,
                showMenuButton = false,
                scrollBehavior = scrollBehavior
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF2E7D32), Color(0XFF4CAF50))
                    )
                )
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(spacing.xl))

            DynamicInfoCard(
                title = "What is SUKI membership?",
                content = "Please confirm your identity by entering your password.",
                footerReminder = "* Do not share your password with anyone.",
                backgroundColor = Color.White.copy(alpha = 0.1f),
                icon = Icons.Filled.Lock
            )
        }
    }

}