package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.repository.LoginRepository

class LoginViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = AppDatabase.get(app).registroUsuarioDao() // ✅ Usa la misma base del registro
        val repo = LoginRepository(dao)
        return LoginViewModel(repo) as T
    }
}
