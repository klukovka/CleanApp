package com.example.cleanapp.data.register.remote

import com.example.cleanapp.data.login.remote.api.LoginApi
import com.example.cleanapp.data.login.repository.LoginRepositoryImpl
import com.example.cleanapp.data.register.remote.api.RegisterApi
import com.example.cleanapp.data.register.repository.RegisterRepositoryImpl
import com.example.cleanapp.domain.common.module.NetworkModule
import com.example.cleanapp.domain.repository.LoginRepository
import com.example.cleanapp.domain.repository.RegisterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class RegisterModule {

    @Singleton
    @Provides
    fun provideRegisterApi(retrofit: Retrofit) : RegisterApi {
        return retrofit.create(RegisterApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRegisterRepository(registerApi: RegisterApi) : RegisterRepository {
        return RegisterRepositoryImpl(registerApi)
    }
}