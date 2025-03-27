package com.example.staysafe.accounts.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phoneNumber: String,
    val password: String,
)

@Entity(tableName = "contacts")
data class UserContact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,  // Foreign key to User
    val contactId: Int // Foreign key to another User (contact)
)

@Entity(tableName = "emergency_contacts")
data class EmergencyContact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,  // Foreign key to User
    val emergencyContactId: Int // Foreign key to another User (emergency contact)
)