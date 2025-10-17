package com.example.levelup.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.data.LoginDatabase
import com.example.levelup.model.repository.LoginRepository

class
LoginViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = LoginDatabase.getDatabase(app).loginDao()
        val repo = LoginRepository(dao)
        return LoginViewModel(repo) as T
    }
}
