package com.example.cleanapp.data.register.repository

import com.example.cleanapp.data.login.remote.dto.LoginResponse
import com.example.cleanapp.data.register.remote.api.RegisterApi
import com.example.cleanapp.data.register.remote.dto.RegisterRequest
import com.example.cleanapp.data.register.remote.dto.RegisterResponse
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.LoginEntity
import com.example.cleanapp.domain.entity.RegisterEntity
import com.example.cleanapp.domain.repository.RegisterRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val registerApi: RegisterApi) :RegisterRepository {
    override suspend fun register(registerRequest: RegisterRequest):
            Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>> {
        return flow {
            val response = registerApi.register(registerRequest)
            if (response.isSuccessful){
                val body = response.body()
                val registerEntity = RegisterEntity(email = body?.data?.email!!,
                    id= body?.data?.id!!, name = body?.data?.name!!,
                    token = body?.data?.token!!)
                emit(BaseResult.Success(registerEntity))
            } else {
                val type = object : TypeToken<WrappedResponse<RegisterResponse>>(){}.type
                val error : WrappedResponse<RegisterResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))

            }
        }
    }
}