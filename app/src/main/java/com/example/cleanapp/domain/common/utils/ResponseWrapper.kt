package com.example.cleanapp.domain.common.utils

data class WrappedListResponse<T>(
    val code: Int,
    val status: Boolean,
    val message: String,
    val errors: List<String>? = null,
    val data: List<T>? = null,
)

data class WrappedResponse<T>(
    val code: Int,
    val status: Boolean,
    val message: String,
    val errors: List<String>? = null,
    val data: T? = null,
)