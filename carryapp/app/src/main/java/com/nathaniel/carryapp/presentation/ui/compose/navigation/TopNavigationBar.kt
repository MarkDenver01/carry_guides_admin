package com.nathaniel.carryapp.presentation.ui.compose.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    navController: NavController,
    title: String,
    showBackButton: Boolean = true,
    showMenuButton: Boolean = true,
    onMenuClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    Surface(
        color = Color(0xFF4CAF50),
        shadowElevation = 4.dp,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp)
                .nestedScroll(scrollBehavior?.nestedScrollConnection ?: TopAppBarDefaults.enterAlwaysScrollBehavior().nestedScrollConnection)
        ) {
            // Left - Menu Icon
            if (showMenuButton) {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }

            // Center - Title
            Text(
                text = title,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 20.sp,
                color = Color.White
            )

            // Right - Back Button
            if (showBackButton) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
