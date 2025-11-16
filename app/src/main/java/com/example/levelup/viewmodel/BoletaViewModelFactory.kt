package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.repository.BoletaRepository
import com.example.levelup.remote.RetrofitBuilder

class BoletaViewModelFactoryApp(private val app: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val db = AppDatabase.getInstance(app)
        val dao = db.boletaDao()
        val api = RetrofitBuilder.boletaApi

        val repo = BoletaRepository(dao, api)

        if (modelClass.isAssignableFrom(BoletaViewModel::class.java)) {
            return BoletaViewModel(app, repo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
