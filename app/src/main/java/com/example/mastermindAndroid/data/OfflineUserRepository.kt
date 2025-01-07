package com.example.mastermindAndroid.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineUserRepository @Inject constructor(private val userDao: UserDao) : UserRepository{
    override suspend fun getAllStream(): Flow<List<User>> = userDao.getAll()

    override fun findByEmailStream(email: String): Flow<User?> = userDao.findByEmail(email)

    override suspend fun insertItem(user: User) = userDao.insert(user)

    override suspend fun deleteItem(user: User) = userDao.delete(user)

    override suspend fun updateItem(user: User) = userDao.update(user)

    suspend fun incrementWins(userId: Int) {
        val user = userDao.getUserById(userId)
        user?.let {
            val updatedUser = it.copy(wins = it.wins + 1)
            userDao.update(updatedUser)
        }
    }

    suspend fun addPoints(userId: Int, points: Int) {
        val user = userDao.getUserById(userId)
        user?.let {
            val updatedUser = it.copy(points = it.points + points)
            userDao.update(updatedUser)
        }
    }


}