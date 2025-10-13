package com.example.levelup.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.condokit.develop.gastoapp.model.local.ExpenseEntity
import cl.condokit.develop.gastoapp.model.repository.ExpenseRepository
import com.example.levelup.local.LoginEntity
import com.example.levelup.reporitory.LoginReporitory
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch



data class FormState(
    val correo: String = "",
    val contrasena: String = "",
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    // val cargando: Boolean = false,
    // val ingresoExitoso: Boolean = false,
    // val sesionActiva: Boolean = false
)

class LoginViewModeel (private val repo: LoginReporitory) : ViewModel(){

    val usuarios: StateFlow<List<LoginEntity>> =
        repo.observarUsuarios().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed( 5_000),
            initialValue = emptyList()
        )

    private val _form = MutableStateFlow(FormState())

    val form: StateFlow<FormState> = _form.asStateFlow()

    fun

}