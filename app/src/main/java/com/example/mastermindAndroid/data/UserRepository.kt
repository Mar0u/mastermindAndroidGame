package com.example.mastermindAndroid.data

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getAllStream(): Flow<List<User>>

    fun findByEmailStream(email: String): Flow<User?>

    suspend fun insertItem(user: User)

    suspend fun deleteItem(user: User)

    suspend fun updateItem(user: User)
}