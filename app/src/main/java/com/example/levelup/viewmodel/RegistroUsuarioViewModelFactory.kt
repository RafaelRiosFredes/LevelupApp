package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.repository.RegistroUsuarioRepository


class RegistroUsuarioViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.get(app)
        val repo = RegistroUsuarioRepository(db.registroUsuarioDao())
        return RegistroUsuarioViewModel(repo) as T
    }
}
