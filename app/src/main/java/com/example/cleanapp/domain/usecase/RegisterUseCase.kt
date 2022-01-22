package com.example.cleanapp.domain.usecase

import com.example.cleanapp.data.register.remote.dto.RegisterRequest
import com.example.cleanapp.data.register.remote.dto.RegisterResponse
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.RegisterEntity
import com.example.cleanapp.domain.repository.RegisterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {
    suspend fun invoke(registerRequest: RegisterRequest)
    : Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>> {
        return registerRepository.register(registerRequest)
    }
}