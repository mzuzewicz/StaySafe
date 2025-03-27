package com.example.staysafe.accounts.data.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {

    //USER ACTIONS
    @Query("Select * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): LiveData<User?>

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User

    @Update
    suspend fun updateUser(user: User)

    @Insert
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)


    //CONTACT ACTIONS
    @Query("Select * FROM users WHERE id IN (SELECT contactId FROM contacts WHERE userId = :userId)")
    suspend fun getContactsForUser(userId: Int): List<User>

    @Query("SELECT * FROM users WHERE id IN(SELECT contactId FROM contacts)")
    suspend fun getAllContacts(): List<User>

    @Insert
    suspend fun insertUserContact(contact: UserContact)

    @Delete
    suspend fun deleteUserContact(contact: UserContact)


    //EMERGENCY CONTACT ACTIONS
    @Query("SELECT * FROM users WHERE id IN (SELECT emergencyContactId FROM emergency_contacts WHERE userId = :userId)")
    suspend fun getEmergencyContactsForUser(userId: Int): List<User>

    @Query("SELECT * FROM users WHERE id IN (SELECT emergencyContactId FROM emergency_contacts)")
    suspend fun getAllEmergencyContacts(): List<User>

    @Insert
    suspend fun insertEmergencyContact(emergencyContact: EmergencyContact)

    @Delete
    suspend fun deleteUserEmergencyContact(contact: UserContact)
}