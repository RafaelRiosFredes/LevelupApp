package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.repository.ProductosRepository
import com.example.levelup.remote.RetrofitBuilder

class ProductosViewModelFactoryApp(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val dao = AppDatabase.getInstance(app).productosDao()
        val api = RetrofitBuilder.productosApi
        val repo = ProductosRepository(dao, api)

        if (modelClass.isAssignableFrom(ProductosViewModel::class.java)) {
            return ProductosViewModel(repo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
