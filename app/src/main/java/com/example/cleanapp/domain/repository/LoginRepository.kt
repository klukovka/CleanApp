package com.example.cleanapp.domain.repository

import com.example.cleanapp.data.login.remote.dto.LoginRequest
import com.example.cleanapp.data.login.remote.dto.LoginResponse
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.LoginEntity
import kotlinx.coroutines.flow.Flow


interface LoginRepository {
    suspend fun login(loginRequest: LoginRequest) : Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>>
}