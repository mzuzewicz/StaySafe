package com.example.staysafe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(navController: NavController, closeDrawer: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .padding(16.dp)) {

        Spacer(modifier = Modifier.height(20.dp))

        Text("Navigation", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            navController.navigate("HomeScreen")
            closeDrawer()
        }) {
            Text("Home")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            navController.navigate("StartJourneyScreen")
            closeDrawer()
        }) {
            Text("Start Journey")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            navController.navigate("AccountScreen")
            closeDrawer()
        }) {
            Text("Account")
        }
    }
}

@Composable
fun AppDrawerLayout(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { ModalDrawerSheet {
            DrawerContent(navController) { scope.launch { drawerState.close() } } }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    title = "StaySafe",
                    navController = navController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                ) },
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    content()
                }
            }
        )
    }
}