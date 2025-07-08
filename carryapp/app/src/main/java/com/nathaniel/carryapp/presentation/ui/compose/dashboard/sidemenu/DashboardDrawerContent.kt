package com.nathaniel.carryapp.presentation.ui.compose.dashboard.sidemenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.presentation.ui.compose.dashboard.DashboardViewModel

@Composable
fun DashboardDrawerContent(
    viewModel: DashboardViewModel,
    onCloseDrawer: () -> Unit
) {
    val pushNotificationsEnabled by viewModel.pushNotificationsEnabled.collectAsState()
    var showPreferences by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_final),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        DrawerItem("My Account", Icons.Default.Person)
        Divider()
        DrawerItem("Change Password", Icons.Default.Lock)
        DrawerItem("Apply Membership", Icons.Default.Star)
        DrawerItem("Address Book", Icons.Default.LocationOn)
        DrawerItem("My Voucher", Icons.Default.Star)
        DrawerItem("My Suki Points", Icons.Default.ThumbUp)

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showPreferences = !showPreferences }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Settings, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Text("Preferences", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (showPreferences) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = "Toggle"
            )
        }

        if (showPreferences) {
            DrawerItem("Support & Feedbacks", Icons.Default.ThumbUp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Push Notifications", modifier = Modifier.weight(1f))
                Switch(
                    checked = pushNotificationsEnabled,
                    onCheckedChange = {
                        viewModel.onTogglePushNotifications(it)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Divider()

        DrawerItem(
            label = "Sign Out",
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onClick = {
                viewModel.onSignOut()
                onCloseDrawer()
            },
            iconTint = Color.Red,
            textColor = Color.Red
        )
    }
}
