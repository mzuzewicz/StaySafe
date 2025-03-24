package com.example.staysafe

import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.staysafe.maps.JourneyRepository
import com.example.staysafe.maps.JourneyViewModel
import com.example.staysafe.maps.api.NetworkModule
import com.example.staysafe.ui.theme.StaySafeTheme


class MainActivity : ComponentActivity() {
    private val requiredPermissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.CALL_PHONE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request permissions
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            // Handle permission results if needed
        }

        // Check and request permissions
        if (!hasRequiredPermissions()) {
            permissionLauncher.launch(requiredPermissions)
        }

        setContent {
            StaySafeTheme {
                MyApp()
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val repository = JourneyRepository(
        context = context,
        directionsApiService = NetworkModule.provideDirectionsApiService()
    )

    val factory = JourneyViewModel.Companion.JourneyViewModelFactory(
        application = context.applicationContext as Application,
        repository = repository
    )

    val journeyViewModel: JourneyViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = "HomeScreen"
    ) {
        composable("HomeScreen") { AppDrawerLayout(navController) { HomeScreen(navController) } }
        composable("StartJourneyScreen") { AppDrawerLayout(navController) { StartJourneyScreen(navController, viewModel = journeyViewModel) } }
        composable("TrackingScreen") {
            AppDrawerLayout(navController) {
                TrackingScreen(journeyViewModel) {
                    navController.navigateUp()
                }
            }
        }
        composable("JourneyCompleteScreen") { AppDrawerLayout(navController) { JourneyCompleteScreen(navController) } }
        composable("AccountScreen") { AppDrawerLayout(navController) { AccountScreen(navController) } }
        composable("TrackFriendScreen") { AppDrawerLayout(navController) { TrackFriendScreen(navController) } }
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


