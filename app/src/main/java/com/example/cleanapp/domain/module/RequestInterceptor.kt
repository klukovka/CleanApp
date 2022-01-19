package com.example.cleanapp.domain.module

import com.example.cleanapp.common.utils.SharedPref
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor constructor(private val pref: SharedPref) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = pref.getToken()
        val request = chain
            .request()
            .newBuilder()
            .addHeader("Authorization", token)
            .build()
        return chain.proceed(request)
    }
}