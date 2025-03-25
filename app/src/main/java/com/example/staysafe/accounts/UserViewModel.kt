package com.example.staysafe.accounts

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val dao: UserDAO) : ViewModel() {
    var users by mutableStateOf(listOf<User>())
    private set

    init {
        viewModelScope.launch {
            dao.getAllUsers().collect { userList ->
                users = userList
            }
        }
    }

    fun addUser(name: String, phoneNumber: String, password: String, contacts: Array<User>, emergencyContacts: Array<User>) {
        viewModelScope.launch {
            dao.insertUser(
                User(name = name,
                    phoneNumber = phoneNumber,
                    password = password,
                    contacts = contacts,
                    emergencyContacts = emergencyContacts
                )
            )
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            dao.updateUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            dao.deleteUser(user)
        }
    }

}