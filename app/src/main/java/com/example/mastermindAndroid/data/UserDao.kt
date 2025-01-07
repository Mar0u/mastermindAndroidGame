package com.example.mastermindAndroid.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY points ASC, wins DESC, games DESC")
    fun getAll(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE email=:email LIMIT 1")
    fun findByEmail(email: String): Flow<User?>

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?
}