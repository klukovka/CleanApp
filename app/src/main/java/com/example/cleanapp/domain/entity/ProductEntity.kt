package com.example.cleanapp.domain.entity

data class ProductEntity(
    val id: Int,
    val price: Int,
    val name: String,
    val user: ProductUserEntity
)