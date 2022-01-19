package com.example.cleanapp.data.login.remote.api

import com.example.cleanapp.data.login.remote.dto.LoginRequest
import com.example.cleanapp.data.login.remote.dto.LoginResponse
import com.example.cleanapp.domain.common.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    abstract suspend fun login(@Body loginRequest: LoginRequest) :
            Response<WrappedResponse<LoginResponse>>

}