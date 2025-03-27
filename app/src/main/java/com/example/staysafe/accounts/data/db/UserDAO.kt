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
    @Query("Select * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Insert
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)


    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): LiveData<User?>

    @Insert
    suspend fun insertUserContact(contact: UserContact)

    @Insert
    suspend fun insertEmergencyContact(emergencyContact: EmergencyContact)


    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User

}