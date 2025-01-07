package com.example.mastermindAndroid.data

import android.content.Context

interface AppContainer {
    val usersRepository: UserRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val usersRepository: UserRepository by lazy {
        OfflineUserRepository(UserDatabase.getDatabase(context).userDao())
    }
}