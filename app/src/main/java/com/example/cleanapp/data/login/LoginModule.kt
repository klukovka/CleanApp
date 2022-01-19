package com.example.cleanapp.data.login

import com.example.cleanapp.data.login.remote.api.LoginApi
import com.example.cleanapp.data.login.repository.LoginRepositoryImpl
import com.example.cleanapp.domain.common.module.NetworkModule
import com.example.cleanapp.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit) : LoginApi{
        return retrofit.create(LoginApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(loginApi: LoginApi) : LoginRepository{
        return LoginRepositoryImpl(loginApi)
    }
}