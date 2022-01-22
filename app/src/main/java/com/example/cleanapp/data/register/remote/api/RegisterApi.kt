package com.example.cleanapp.data.register.remote.api

import com.example.cleanapp.data.register.remote.dto.RegisterRequest
import com.example.cleanapp.data.register.remote.dto.RegisterResponse
import com.example.cleanapp.domain.common.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("auth/register")
    abstract suspend fun register(@Body request: RegisterRequest) :
            Response<WrappedResponse<RegisterResponse>>
}