package com.example.levelup.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.local.AppDatabase
import com.example.levelup.model.reporitory.LoginReporitory

class LoginViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.get(app)
        val repo = LoginReporitory(db.loginDao())
        return LoginViewModel(repo) as T
    }
}