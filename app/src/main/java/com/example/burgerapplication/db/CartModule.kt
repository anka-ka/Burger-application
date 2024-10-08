package com.example.burgerapplication.db

import com.example.burgerapplication.dao.CartDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {

    @Provides
    @Singleton
    fun provideCartDao(appDatabase: CartDatabase): CartDao {
        return appDatabase.cartDao()
    }
}