package com.example.levelup.remote

data class PageRemoteDTO<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Long,
    val size: Int,
    val number: Int
)
