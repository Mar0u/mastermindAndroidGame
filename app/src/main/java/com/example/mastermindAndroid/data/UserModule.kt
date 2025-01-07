package com.example.mastermindAndroid.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UserRepositoryModule {

    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return OfflineUserRepository(userDao)
    }
}