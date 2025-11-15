package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BoletaViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoletaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BoletaViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
