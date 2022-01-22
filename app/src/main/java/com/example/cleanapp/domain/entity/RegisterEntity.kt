package com.example.cleanapp.domain.entity

data class RegisterEntity(
    val email: String,
    val id: Int,
    val name: String,
    val token: String
)