package com.artemklymenko.rickipedia.di

import com.artemklymenko.network.models.core.client.KtorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideKtorClient(): KtorClient {
        return KtorClient()
    }
}