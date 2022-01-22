package com.example.cleanapp.data.register.remote.dto

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String
)