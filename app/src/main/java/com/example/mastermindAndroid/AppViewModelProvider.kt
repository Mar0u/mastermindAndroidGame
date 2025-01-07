package com.example.mastermindAndroid

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mastermindAndroid.data.ProfileViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            ProfileViewModel(
                gameApplication().container.usersRepository
            )
        }
    }
}

fun CreationExtras.gameApplication(): GameApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GameApplication)