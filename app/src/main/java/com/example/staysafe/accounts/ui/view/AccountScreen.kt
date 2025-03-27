package com.example.staysafe.accounts.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.staysafe.accounts.data.db.User
import com.example.staysafe.accounts.ui.viewmodel.UserViewModel


@Composable
fun AccountScreen(navController: NavController, viewModel: UserViewModel) {
    val user by viewModel.user.observeAsState()
    var name by remember { mutableStateOf(user?.name ?: "") }
    var password by remember { mutableStateOf(user?.password ?: "") }
    var phoneNumber by remember { mutableStateOf(user?.phoneNumber ?: "")}
    var contactName by remember { mutableStateOf("") }
    var emergencyContactName by remember { mutableStateOf("") }


    val contacts = remember { mutableStateListOf<User>() }
    val emergencyContacts = remember { mutableStateListOf<User>() }


    Column(modifier = Modifier.padding(16.dp)) {
        Text("User Profile", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(value = phoneNumber,
            onValueChange = {phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Contacts:", style = MaterialTheme.typography.labelMedium)
        contacts.forEachIndexed { index, contact ->
            Text("${index + 1}. ${contact.name}")
        }

        OutlinedTextField(
            value = contactName,
            onValueChange = { contactName = it },
            label = { Text("Add Contact") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            if (contactName.isNotBlank()) {
                contacts.add(User(name = contactName, password = password, phoneNumber = phoneNumber))
                contactName = ""
            }
        }) {
            Text("Add Contact")
        }

        // Emergency Contacts Section
        Text("Emergency Contacts:", style = MaterialTheme.typography.labelMedium)
        emergencyContacts.forEachIndexed { index, contact ->
            Text("${index + 1}. ${contact.name}")
        }

        OutlinedTextField(
            value = emergencyContactName,
            onValueChange = { emergencyContactName = it },
            label = { Text("Add Emergency Contact") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = {
            if (emergencyContactName.isNotBlank()) {
                emergencyContacts.add(User(name = contactName, password = password, phoneNumber = phoneNumber))
                emergencyContactName = ""
            }
        }) {
            Text("Add Emergency Contact")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Button(
                onClick = {
                    viewModel.addOrUpdateUser(
                        User(name = name, password = password, phoneNumber = phoneNumber)
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    user?.let { viewModel.deleteUser(it) }
                },
                modifier = Modifier.weight(1f),
            ) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
