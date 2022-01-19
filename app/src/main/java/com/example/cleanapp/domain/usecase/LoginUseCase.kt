package com.example.cleanapp.domain.usecase

import com.example.cleanapp.data.login.remote.dto.LoginRequest
import com.example.cleanapp.data.login.remote.dto.LoginResponse
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.LoginEntity
import com.example.cleanapp.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun invoke(loginRequest: LoginRequest) : Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>>{
        return loginRepository.login(loginRequest)
    }
}