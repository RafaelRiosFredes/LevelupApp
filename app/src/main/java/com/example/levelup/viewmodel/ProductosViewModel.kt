package com.example.levelup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.model.repository.ProductosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductosViewModel(
    private val repository: ProductosRepository
) : ViewModel() {


    //   LISTA DE PRODUCTOS (ROOM)
    val productos = repository.obtenerProductos()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    private val _categorias = kotlinx.coroutines.flow.MutableStateFlow<List<com.example.levelup.remote.CategoriaRemoteDTO>>(emptyList())
    val categorias = _categorias.asStateFlow()

    // =============================
    //   CRUD LOCAL (Room)
    // =============================

    fun insertarProducto(producto: ProductosEntity) = viewModelScope.launch {
        repository.insertarProducto(producto)
    }

    fun obtenerProductoPorId(id: Long): Flow<ProductosEntity?> {
        return repository.obtenerProductoPorId(id)
    }

    fun actualizarProducto(producto: ProductosEntity) = viewModelScope.launch {
        repository.actualizarProducto(producto)
    }

    fun eliminarProducto(producto: ProductosEntity) = viewModelScope.launch {
        repository.eliminarProducto(producto)
    }


    // =============================
    //   CRUD BACKEND (Retrofit)
    // =============================

    fun crearProductoBackend(producto: ProductosEntity) = viewModelScope.launch {
        repository.crearProducto(producto)
    }

    fun actualizarProductoBackend(producto: ProductosEntity) = viewModelScope.launch {
        repository.actualizarProductoBackend(producto)
    }

    fun eliminarProductoBackend(producto: ProductosEntity) = viewModelScope.launch {
        repository.eliminarProductoBackend(producto)
    }

    fun obtenerProductoBackend(id: Long) = viewModelScope.launch {
        repository.obtenerProductoDesdeBackend(id)
    }


    // =============================
    //   SINCRONIZACIÓN OPCIONAL
    // =============================

    fun sincronizarProductos() = viewModelScope.launch {
        try {
            repository.sincronizarProductosDesdeBackend()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        sincronizarProductos()
    }

    fun crearProductoConImagen(
        producto: ProductosEntity,
        base64: String,
        nombreArchivo: String,
        contentType: String,
        onResult: (Boolean) -> Unit
    ) = viewModelScope.launch {
        try {

            // Usamos el método correcto del Repository
            val creado = repository.crearProductoConImagen(
                producto,
                base64,
                nombreArchivo,
                contentType
            )

            // Si todo salió bien
            if (creado != null) {
                sincronizarProductos()   // refrescar Room con imagen incluida
                onResult(true)
            } else {
                onResult(false)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            onResult(false)
        }
    }


    fun cargarCategorias() = viewModelScope.launch {
        _categorias.value = repository.obtenerCategoriasBackend()
    }

    // 3. Asegúrate de llamar a cargarCategorias en el init
    init {
        sincronizarProductos()
        cargarCategorias() // <--- AGREGAR ESTO
    }
}
