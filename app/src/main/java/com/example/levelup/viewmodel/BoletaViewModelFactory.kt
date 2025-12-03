package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BoletaViewModelFactoryApp(
    private val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(BoletaViewModel::class.java)) {
            return BoletaViewModel(app) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
