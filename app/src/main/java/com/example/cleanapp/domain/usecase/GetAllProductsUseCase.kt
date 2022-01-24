package com.example.cleanapp.domain.usecase

import com.example.cleanapp.data.products.remote.dto.ProductResponse
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedListResponse
import com.example.cleanapp.domain.entity.ProductEntity
import com.example.cleanapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend fun invoke()
            : Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>>{
        return productRepository.getAllMyProducts()
    }
}