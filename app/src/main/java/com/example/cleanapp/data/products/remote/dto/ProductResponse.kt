package com.example.cleanapp.data.products.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    val id: Int,
    val price: Int,
    @SerializedName("product_name") val productName: String,
    val user: ProductUserResponse
)