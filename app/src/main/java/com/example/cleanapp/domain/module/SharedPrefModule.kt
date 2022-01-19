package com.example.cleanapp.domain.module

import android.content.Context
import com.example.cleanapp.common.utils.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {

    @Provides
    fun provideSharedPref (@ApplicationContext context : Context) : SharedPref{
        return SharedPref(context)
    }
}