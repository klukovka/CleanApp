package com.example.cleanapp.domain.usecase

import com.example.cleanapp.data.products.remote.dto.ProductResponse
import com.example.cleanapp.data.products.remote.dto.ProductUpdateRequest
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.ProductEntity
import com.example.cleanapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend fun invoke(productUpdateRequest: ProductUpdateRequest, id: String)
            : Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>{
        return productRepository.updateProduct(productUpdateRequest, id)
    }
}