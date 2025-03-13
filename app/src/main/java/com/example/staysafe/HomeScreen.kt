package com.example.staysafe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    var presses by remember { mutableIntStateOf(0) }


    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text =
                """
                    Welcome to StaySafe.

                """.trimIndent(),

                )
            Row {
                val startJourneyButton = Button(
                    onClick = {navController.navigate("StartJourneyScreen")})
                { Text("Start Journey") }
                val trackJourneyButton = Button(onClick = {navController.navigate("TrackingScreen")}) { Text("Track Journey") }
            }
        }
    }
}