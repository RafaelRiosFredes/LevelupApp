package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductosViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //Verifica si viewmodel solicitado es productoviewmodel
        if (modelClass.isAssignableFrom(ProductosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")//crea y devuelve el viewmodel
            return ProductosViewModel(application) as T
        }
        //lanza error si viewmodel es desconocido
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}