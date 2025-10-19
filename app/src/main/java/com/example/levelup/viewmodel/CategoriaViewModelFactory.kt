package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.repository.CategoriaRepository

class CategoriaViewModelFactory(private val app: Application) : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        val db = AppDatabase.get(app)
        val repo = CategoriaRepository(db.categoriaDao())
        return CategoriaViewModel(repo) as T
    }
}