package com.algebra.wheathertask.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ExecutorsModule {

    @DatabaseIOExecutor
    @Singleton
    @Provides
    fun provideApiServerExecutor(): Executor{
        return Executors.newSingleThreadExecutor()
    }
}