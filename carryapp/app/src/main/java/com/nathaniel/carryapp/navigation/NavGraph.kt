package com.nathaniel.carryapp.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nathaniel.carryapp.presentation.ui.compose.account.display_profile.DisplayProfileScreen
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.DashboardScreen
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.delivery.DeliveryScreen
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.pickup.PickupScreen
import com.nathaniel.carryapp.presentation.ui.compose.initial.InitialScreen
import com.nathaniel.carryapp.presentation.ui.compose.signin.SignInScreen
import com.nathaniel.carryapp.presentation.ui.compose.signup.SignUpScreen

fun NavGraphBuilder.initialGraph(navController: NavController) {
    composable(Routes.INITIAL) {
        InitialScreen(navController = navController)
    }
}

fun NavGraphBuilder.dashboardGraph(navController: NavController) {
    composable(Routes.DASHBOARD) {
        DashboardScreen(navController = navController)
    }
    composable(Routes.DELIVERY) {
        DeliveryScreen(navController = navController)
    }
    composable(Routes.PICKUP) {
        PickupScreen(navController = navController)
    }
}

fun NavGraphBuilder.signUpGraph(navController: NavController) {
    composable(Routes.SIGN_UP) {
        SignUpScreen(navController = navController)
    }
}

fun NavGraphBuilder.signInGraph(navController: NavController) {
    composable(Routes.SIGN_IN) {
        SignInScreen(navController = navController)
    }
}

fun NavGraphBuilder.displayUserProfileGraph(navController: NavController) {
    composable(Routes.DISPLAY_PROFILE) {
        DisplayProfileScreen(navController = navController)
    }
}