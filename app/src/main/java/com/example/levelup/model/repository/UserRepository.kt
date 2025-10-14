package com.example.levelup.model.repository

import com.example.levelup.model.local.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepository {
    private var currentUser: UserEntity? = null

    fun login(email: String, password: String): UserEntity {
        // Simular login — luego reemplaza con llamada a API o DB
        currentUser = UserEntity(email, "Rafael", 120)
        return currentUser!!
    }

    fun getCurrentUser(): UserEntity? = currentUser
}
