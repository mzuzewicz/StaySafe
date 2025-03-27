package com.example.staysafe.accounts.data.repository


import androidx.lifecycle.LiveData
import com.example.staysafe.accounts.data.db.EmergencyContact
import com.example.staysafe.accounts.data.db.User
import com.example.staysafe.accounts.data.db.UserContact
import com.example.staysafe.accounts.data.db.UserDAO

class UserRepository(private val userDao: UserDAO){

    val user: LiveData<User?> = userDao.getUser()

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }


    suspend fun insertUserContact(contact: UserContact) {
        userDao.insertUserContact(contact)
    }

    suspend fun insertEmergencyContact(emergencyContact: EmergencyContact) {
        userDao.insertEmergencyContact(emergencyContact)
    }

    suspend fun insertUserWithContacts(
        user: User,
        contacts: List<User>,
        emergencyContacts: List<User>
    ) {
        insertUser(user)
        contacts.forEach { contact ->
            insertUserContact(UserContact(userId = user.id, contactId = contact.id))
        }
        emergencyContacts.forEach { emergencyContact ->
            insertEmergencyContact(EmergencyContact(userId = user.id, emergencyContactId = emergencyContact.id))
        }
    }
    suspend fun insertData() {

        val contact1 = User(name = "Alice", phoneNumber = "1234567890", password = "password123")
        val contact2 = User(name = "Bob", phoneNumber = "9876543210", password = "password456")

        val emergencyContact1 = User(name = "Eve", phoneNumber = "5551234567", password = "password789")

        val newUser = User(name = "John Doe", phoneNumber = "5559876543", password = "john123")

        insertUserWithContacts(
            user = newUser,
            contacts = listOf(contact1, contact2),
            emergencyContacts = listOf(emergencyContact1)
        )
    }

}
