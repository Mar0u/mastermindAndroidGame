package com.example.mastermindAndroid.data

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var user = mutableStateOf(User())
    var guessColors = mutableIntStateOf(5)
    lateinit var users: Flow<List<User>>

    fun loadUsers() {
        viewModelScope.launch {
            users = userRepository.getAllStream()
        }
    }

    fun insertUser(userI: User) {
        viewModelScope.launch {
            userRepository.insertItem(user = userI)
            getUser(userI.email)
        }
    }

    suspend fun getUser(email: String) {
        val temp = userRepository.findByEmailStream(email).firstOrNull()
        if (temp != null)
            user.value = temp
    }

    fun updateUser() {
        viewModelScope.launch {
            userRepository.updateItem(user = user.value)
        }
    }

    fun incrementUserWins(points: Int) {
        viewModelScope.launch {
            val currentUser = user.value
            val updatedUser = currentUser.copy(wins = currentUser.wins + 1, points = currentUser.points + points)
            userRepository.updateItem(updatedUser)
            user.value = updatedUser
        }
    }

}