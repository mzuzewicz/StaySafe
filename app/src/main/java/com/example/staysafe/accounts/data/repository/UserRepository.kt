package com.example.staysafe.accounts.data.repository


import android.util.Log
import androidx.lifecycle.LiveData
import com.example.staysafe.accounts.data.db.EmergencyContact
import com.example.staysafe.accounts.data.db.User
import com.example.staysafe.accounts.data.db.UserContact
import com.example.staysafe.accounts.data.db.UserDAO

class UserRepository(private val userDao: UserDAO){

    val user: LiveData<User?> = userDao.getUser()

    //USER ACTIONS
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }


    //CONTACT ACTIONS
    suspend fun insertContact(contact: UserContact) {
        userDao.insertUserContact(contact)
    }

    suspend fun updateContact(contact: UserContact) {
        userDao.updateContact(contact)
    }

    suspend fun deleteContact(contact: UserContact) {
        userDao.deleteUserContact(contact)
    }



    //EMERGENCY CONTACT ACTIONS
    suspend fun insertEmergencyContact(emergencyContact: EmergencyContact) {
        userDao.insertEmergencyContact(emergencyContact)
    }

    suspend fun updateEmergencyContact(emergencyContact: EmergencyContact) {
        userDao.updateEmergencyContact(emergencyContact)
    }

    suspend fun deleteEmergencyContact(emergencyContact: EmergencyContact) {
        userDao.deleteUserEmergencyContact(emergencyContact)
    }



    //DUMMY DATA
    suspend fun insertDummyData() {
            val contact1 = UserContact(contactName = "Jeff", contactPhoneNumber = "239480293845")
            val contact2 = UserContact(contactName = "Mike", contactPhoneNumber = "212445656754")
            val contact3 = UserContact(contactName = "Bob", contactPhoneNumber = "345623426346")
            val contact4 = UserContact(contactName = "Larry", contactPhoneNumber = "34534735423")

            val emergencyContact1 = EmergencyContact(contactName = "Mum", contactPhoneNumber = "34230472350")
            val newUser = User(name = "John Doe", phoneNumber = "5559876543", password = "john123")

            userDao.insertUser(user = newUser)
            userDao.insertUserContact(contact1)
            userDao.insertUserContact(contact2)
            userDao.insertUserContact(contact3)
            userDao.insertUserContact(contact4)
            userDao.insertEmergencyContact(emergencyContact1)
    }
}
