package com.nathaniel.carryapp.presentation.ui.compose.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem.item.forEach { item ->
                val isSelected = item.route == currentRoute
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            if (!isSelected) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
        }
    }
}
