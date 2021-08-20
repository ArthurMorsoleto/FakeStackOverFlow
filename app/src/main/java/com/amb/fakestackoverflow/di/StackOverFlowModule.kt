package com.amb.fakestackoverflow.di

import com.amb.fakestackoverflow.model.StackOverFlowService
import dagger.Module
import dagger.Provides

@Module
class StackOverFlowModule {

    @Provides
    fun providesStackOverFlowService() = StackOverFlowService
}