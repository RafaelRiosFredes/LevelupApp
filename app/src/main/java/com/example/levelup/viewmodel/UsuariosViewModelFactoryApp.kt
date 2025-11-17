package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.repository.UsuariosRepository
import com.example.levelup.remote.RetrofitBuilder

class UsuariosViewModelFactoryApp(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val db = AppDatabase.getInstance(app)
        val dao = db.usuarioDao()
        val api = RetrofitBuilder.usuariosApi

        val repo = UsuariosRepository(dao, api, authApi = RetrofitBuilder.authApi)

        if (modelClass.isAssignableFrom(UsuariosViewModel::class.java)) {
            return UsuariosViewModel(app, repo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
