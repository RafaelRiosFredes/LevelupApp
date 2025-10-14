package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.local.AppDatabase
import com.example.levelup.model.repository.ProductosRepository
import kotlin.collections.get

class ProductosViewModelFactor(private val app: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.get(app)
        val repo = ProductosRepository(db.productosDao())
        return ProductosViewModel(repo) as T
    }
}