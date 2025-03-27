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

    @Insert
    suspend fun insertUserById(user: User): Long

    @Delete
    suspend fun deleteUser(user: User)



    @Insert
    suspend fun insertUserContact(contact: UserContact)

    @Update
    suspend fun updateContact(contact: UserContact)

    @Delete
    suspend fun deleteUserContact(contact: UserContact)



    @Insert
    suspend fun insertEmergencyContact(emergencyContact: EmergencyContact)

    @Update
    suspend fun updateEmergencyContact(emergencyContact: EmergencyContact)

    @Delete
    suspend fun deleteUserEmergencyContact(emergencyContact: EmergencyContact)
}