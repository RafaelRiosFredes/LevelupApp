package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.repository.ProductosRepository

class ProductosViewModelFactoryApp(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.get(app)
        val repo = ProductosRepository(db.productoDao())
        if (modelClass.isAssignableFrom(ProductosViewModel::class.java)) {
            return ProductosViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
