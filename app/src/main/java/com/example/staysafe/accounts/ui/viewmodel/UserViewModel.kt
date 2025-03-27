package com.example.staysafe.accounts.ui.viewmodel


import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.staysafe.accounts.data.db.User
import com.example.staysafe.accounts.data.repository.UserRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel


class UserViewModel(application: Application, private val repository: UserRepository) : AndroidViewModel(application) {
    val user: LiveData<User?> = repository.user

    fun addOrUpdateUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }

    fun insertDummyData() {
        viewModelScope.launch {
            repository.insertData()
        }
    }

    fun insertUserWithContacts(user: User, contacts: List<User>, emergencyContacts: List<User>) {
        viewModelScope.launch {
            repository.insertUserWithContacts(user, contacts, emergencyContacts)
        }
    }

    companion object{
        class UserViewModelFactory(
            private val application: Application,
            private val repository: UserRepository
        ) : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                    return UserViewModel(application, repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }


}

