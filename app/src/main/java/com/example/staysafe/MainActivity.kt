package com.example.staysafe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.staysafe.ui.theme.StaySafeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StaySafeTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "HomeScreen"
    ) {
        composable("HomeScreen") { AppDrawerLayout(navController) { HomeScreen(navController) } }
        composable("StartJourneyScreen") { AppDrawerLayout(navController) { StartJourneyScreen(navController) } }
        composable("TrackingScreen") { AppDrawerLayout(navController) { TrackingScreen(navController) } }
        composable("JourneyCompleteScreen") { AppDrawerLayout(navController) { JourneyCompleteScreen(navController) } }
        composable("AccountScreen") { AppDrawerLayout(navController) { AccountScreen(navController) } }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    navController: NavController,
    onMenuClick: () -> Unit
) {
    val currentDestination = navController.currentBackStackEntry?.destination?.route

    TopAppBar(title = { Text(title) },
        navigationIcon = {
            if (currentDestination != "HomeScreen") {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Filled.Menu,  contentDescription = "Menu")
            }
        }
    )
}


