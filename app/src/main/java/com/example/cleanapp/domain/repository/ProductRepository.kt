package com.example.cleanapp.domain.repository

import com.example.cleanapp.data.products.remote.dto.ProductCreateRequest
import com.example.cleanapp.data.products.remote.dto.ProductResponse
import com.example.cleanapp.data.products.remote.dto.ProductUpdateRequest
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedListResponse
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllMyProducts()
            : Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>>

    suspend fun getProductById(id: String)
            : Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>

    suspend fun updateProduct(productUpdateRequest: ProductUpdateRequest)
            : Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>

    suspend fun deleteProduct(id: String)
            : Flow<BaseResult<Unit, WrappedResponse<ProductResponse>>>

    suspend fun createProduct(productCreateRequest: ProductCreateRequest)
            : Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>

}