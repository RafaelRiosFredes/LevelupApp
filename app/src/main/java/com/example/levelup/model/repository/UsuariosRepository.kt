package com.example.levelup.model.repository

import com.example.levelup.model.data.UsuarioDao
import com.example.levelup.model.data.UsuarioEntity
import kotlinx.coroutines.flow.Flow

class UsuariosRepository(private val dao: UsuarioDao) {
    fun todosLosUsuarios(): Flow<List<UsuarioEntity>> = dao.obtenerTodos()

    fun obtenerPorId(id: Int): Flow<UsuarioEntity?> = dao.obtenerPorId(id)

    suspend fun insertar(usuario: UsuarioEntity) = dao.insertarUsuario(usuario)

    suspend fun actualizar(usuario: UsuarioEntity) = dao.actualizarUsuario(usuario)

    suspend fun eliminar(usuario: UsuarioEntity) = dao.eliminarUsuario(usuario)

    suspend fun eliminarPorCorreo(correo: String) = dao.eliminarPorCorreo(correo)

    suspend fun obtenerPorCorreo(correo: String) = dao.obtenerPorCorreo(correo)
}
