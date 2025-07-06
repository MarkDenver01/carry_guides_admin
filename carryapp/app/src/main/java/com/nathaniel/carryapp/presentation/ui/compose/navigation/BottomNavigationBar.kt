package com.nathaniel.carryapp.presentation.ui.compose.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nathaniel.carryapp.presentation.ui.compose.navigation.BottomNavItem
import com.nathaniel.carryapp.presentation.ui.compose.navigation.BottomNavItem.Alerts.icon

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentDestination = navController
        .currentBackStackEntryAsState().value?.destination?.route

    BottomAppBar(
        containerColor = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
       BottomNavItem.item.forEach { item ->
           NavigationBarItem(
               icon = { Icon(item.icon, contentDescription = item.label) },
               label = { Text(item.label, fontSize = 12.sp) },
               selected = currentDestination == item.route,
               onClick = {
                   if (currentDestination != item.route) {
                       navController.navigate(item.route) {
                           popUpTo(navController.graph.startDestinationId) {
                               saveState = true
                           }
                           launchSingleTop = true
                           restoreState = true
                       }
                   }
               }
           )

       }
    }
}
