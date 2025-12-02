package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.levelup.model.repository.OpinionesRepository

class OpinionesViewModelFactory(
    private val repo: OpinionesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OpinionesViewModel::class.java)) {
            return OpinionesViewModel(repo) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
