package com.example.staysafe.accounts.ui.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.staysafe.accounts.data.db.User
import com.example.staysafe.accounts.data.repository.UserRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class UserViewModel(application: Application, private val repository: UserRepository) :
    AndroidViewModel(application) {
    val user: LiveData<User?> = repository.user
    private val _contacts = MutableStateFlow<List<User>>(emptyList())
    val contacts: StateFlow<List<User>> = _contacts.asStateFlow()

    init {
    }

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


    companion object {
        class UserViewModelFactory(
            private val application: Application,
            private val repository: UserRepository
        ) : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                    return UserViewModel(application, repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }


}

