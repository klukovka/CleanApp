package com.example.cleanapp.data.products.remote.api

import com.example.cleanapp.data.products.remote.dto.ProductCreateRequest
import com.example.cleanapp.data.products.remote.dto.ProductResponse
import com.example.cleanapp.data.products.remote.dto.ProductUpdateRequest
import com.example.cleanapp.domain.common.utils.WrappedListResponse
import com.example.cleanapp.domain.common.utils.WrappedResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {

    @GET("product/")
    suspend fun getAllUserProducts() : Response<WrappedListResponse<ProductResponse>>

    @GET("product/{id}")
    suspend fun getProductById(@Path("id") id : String) : Response<WrappedResponse<ProductResponse>>

    @PUT("product/{id}")
    suspend fun updateProduct(@Path("id") id : String,
    @Body productUpdateRequest: ProductUpdateRequest) : Response<WrappedResponse<ProductResponse>>

    @DELETE("product/{id}")
    suspend fun deleteProduct(@Path("id") id : String) : Response<WrappedResponse<ProductResponse>>

    @POST("product/")
    suspend fun createProduct(@Body productCreateRequest: ProductCreateRequest) : Response<WrappedResponse<ProductResponse>>

}