package com.example.staysafe

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.staysafe.maps.Journey
import com.example.staysafe.maps.JourneyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackFriendScreen(navController: NavController, viewModel: JourneyViewModel) {
    val contacts = listOf(
        FriendContact("James Smith", "+44 7911 123456", "London, UK", "Manchester, UK", "Birmingham, UK"),
        FriendContact("Olivia Johnson", "+44 7720 987654", "Liverpool, UK", "Edinburgh, UK", "Leeds, UK"),
        FriendContact("William Brown", "+44 7500 112233", "Bristol, UK", "Cardiff, UK", "Bath, UK"),
        FriendContact("Sophia Wilson", "+44 7405 667788", "Nottingham, UK", "Leicester, UK", "Sheffield, UK"),
        FriendContact("Benjamin Taylor", "+44 7901 556677", "Oxford, UK", "Cambridge, UK", "Milton Keynes, UK")
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Contacts") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(contacts) { contact ->
                ContactItem(contact) {
                    val journey = Journey(contact.startLocation, contact.endLocation, "", "", 1)
                    viewModel.setJourney(journey)
                    //pass variables to next screen
                    navController.navigate("FriendTrackingScreen?departureHour=06&departureMinute=$30&arrivalHour=$60&arrivalMinute=$30")
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: FriendContact, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Contact Icon",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = contact.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(text = contact.phoneNumber, color = Color.Gray, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Start: ${contact.startLocation}", fontSize = 14.sp, color = Color.DarkGray)
            Text(text = "End: ${contact.endLocation}", fontSize = 14.sp, color = Color.DarkGray)
            Text(text = "Current: ${contact.currentLocation}", fontSize = 14.sp, color = Color.Red)
        }
    }
}

