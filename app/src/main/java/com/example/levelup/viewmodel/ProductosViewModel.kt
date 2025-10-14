package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.local.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FormState(
    val id: Int? = null,
    val descripcion: String = "",
    val monto: String = "",
    val categoria: String = "",
    val error: String? = null
)

class ProductosViewModel(private val repo: ProductosRepository) : ViewModel() {

    val productos: StateFlow<List<ProductosEntity>> =
        repo.observarProductos().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _form = MutableStateFlow(FormState())

    val form: StateFlow<FormState> = _form.asStateFlow()

    fun cargarParaEditar(expense: ProductosEntity) {
        _form.value = FormState(
            id = expense.id,
            descripcion = expense.descripcion,
            monto = expense.monto.toString(),
            categoria = expense.categoria
        )
    }

    fun limpiarFormulario() = run { _form.value = FormState() }

    fun onChangeDescripcion(v: String) = _form.update { it.copy(descripcion = v) }
    fun onChangeMonto(v: String)       = _form.update { it.copy(monto = v) }
    fun onChangeCategoria(v: String)   = _form.update { it.copy(categoria = v) }


    /*fun guardar() = viewModelScope.launch {
        val f = _form.value
        val montoDouble = f.monto.toDoubleOrNull()
        if (f.descripcion.isBlank() || montoDouble == null ||
            f.categoria.isBlank()
        ) {
            _form.update { it.copy(error = "Completa todos los campos. Monto debe ser numérico.") }
            return@launch
        }

        repo.guardar(f.id, f.descripcion,  montoDouble, f.categoria)
        limpiarFormulario()
    }*/

    fun eliminar(productos: ProductosEntity) = viewModelScope.launch { repo.eliminar(productos) }
}
