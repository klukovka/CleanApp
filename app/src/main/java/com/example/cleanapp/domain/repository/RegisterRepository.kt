package com.example.cleanapp.domain.repository

import com.example.cleanapp.data.register.remote.dto.RegisterRequest
import com.example.cleanapp.data.register.remote.dto.RegisterResponse
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.RegisterEntity
import kotlinx.coroutines.flow.Flow

interface RegisterRepository {
    suspend fun register(registerRequest: RegisterRequest)
    : Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>>
}