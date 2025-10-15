package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.local.AppDatabase
import com.example.levelup.model.repository.ProductosRepository
import kotlin.collections.get

class ProductosViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductosViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}