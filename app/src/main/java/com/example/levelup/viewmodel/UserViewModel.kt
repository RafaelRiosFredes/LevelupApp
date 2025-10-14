package com.example.levelup.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.levelup.model.local.UserEntity
import com.example.levelup.model.repository.UserRepository

class UserViewModel : ViewModel() {
    private val repo = UserRepository()

    private val _user = mutableStateOf<UserEntity?>(null)
    val user: State<UserEntity?> = _user

    fun login(email: String, password: String) {
        _user.value = repo.login(email, password)
    }

    fun logout() {
        _user.value = null
    }
}