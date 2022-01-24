package com.example.cleanapp.data.products.repository

import com.example.cleanapp.data.login.remote.dto.LoginResponse
import com.example.cleanapp.data.products.remote.api.ProductApi
import com.example.cleanapp.data.products.remote.dto.ProductCreateRequest
import com.example.cleanapp.data.products.remote.dto.ProductResponse
import com.example.cleanapp.data.products.remote.dto.ProductUpdateRequest
import com.example.cleanapp.domain.common.base.BaseResult
import com.example.cleanapp.domain.common.utils.WrappedListResponse
import com.example.cleanapp.domain.common.utils.WrappedResponse
import com.example.cleanapp.domain.entity.ProductEntity
import com.example.cleanapp.domain.entity.ProductUserEntity
import com.example.cleanapp.domain.repository.ProductRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productApi : ProductApi) :
    ProductRepository {
    override suspend fun getAllMyProducts(): Flow<BaseResult<List<ProductEntity>, WrappedListResponse<ProductResponse>>> {
        return flow{
            val response = productApi.getAllUserProducts()

            if (response.isSuccessful){

                val body = response.body()
                val products = mutableListOf<ProductEntity>()

                body?.data?.forEach { product ->
                    var user = ProductUserEntity(product.user.email, product.user.id, product.user.name)
                    products.add(ProductEntity(product.id, product.price, product.productName, user))
                }

                emit(BaseResult.Success(products))

            } else {
                val type = object : TypeToken<WrappedListResponse<ProductResponse>>(){}.type
                val error : WrappedListResponse<ProductResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }

    override suspend fun getProductById(id: String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return flow {
            var response = productApi.getProductById(id)

            if (response.isSuccessful){
                val data = response.body()?.data!!

                val userEntity = ProductUserEntity(data.user.email, data.user.id, data.user.name)

                val product = ProductEntity(data.id, data.price, data.productName, userEntity)

                emit(BaseResult.Success(product))

            } else {
                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
                val error : WrappedResponse<ProductResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }

    override suspend fun updateProduct(
        productUpdateRequest: ProductUpdateRequest,
        id: String
    ): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return flow {
            var response = productApi.updateProduct(id, productUpdateRequest)

            if (response.isSuccessful){
                val data = response.body()?.data!!

                val userEntity = ProductUserEntity(data.user.email, data.user.id, data.user.name)

                val product = ProductEntity(data.id, data.price, data.productName, userEntity)

                emit(BaseResult.Success(product))

            } else {
                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
                val error : WrappedResponse<ProductResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }

    override suspend fun deleteProduct(id: String): Flow<BaseResult<Unit, WrappedResponse<ProductResponse>>> {
        return flow {
            var response = productApi.deleteProduct(id)

            if (response.isSuccessful){

                emit(BaseResult.Success(Unit))

            } else {
                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
                val error : WrappedResponse<ProductResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }

    override suspend fun createProduct(productCreateRequest: ProductCreateRequest): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return flow {
            var response = productApi.createProduct(productCreateRequest)

            if (response.isSuccessful){
                val data = response.body()?.data!!

                val userEntity = ProductUserEntity(data.user.email, data.user.id, data.user.name)

                val product = ProductEntity(data.id, data.price, data.productName, userEntity)

                emit(BaseResult.Success(product))

            } else {
                val type = object : TypeToken<WrappedResponse<ProductResponse>>(){}.type
                val error : WrappedResponse<ProductResponse> =
                    Gson().fromJson(response.errorBody()!!.charStream(), type)!!
                error.code = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }

}