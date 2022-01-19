package com.example.cleanapp.data.login.repository

import com.example.cleanapp.data.login.remote.api.LoginApi
import com.example.cleanapp.data.login.remote.dto.LoginRequest
import com.example.cleanapp.data.login.remote.dto.LoginResponse
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.LoginEntity
import com.example.cleanapp.domain.repository.LoginRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val loginApi: LoginApi) : LoginRepository {
    override suspend fun login(loginRequest: LoginRequest):
            Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>> {
        return flow {
            val response = loginApi.login(loginRequest)
            if (response.isSuccessful){
                val body = response.body()
                val loginEntity = LoginEntity(email = body?.data?.email!!,
                id= body?.data?.id!!, name = body?.data?.name!!,
                token = body?.data?.token!!)
                emit(BaseResult.Success(loginEntity))
            } else {
                val type = object : TypeToken<WrappedResponse<LoginResponse>>(){}.type
                val error : WrappedResponse<LoginResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))

            }
        }
    }
}