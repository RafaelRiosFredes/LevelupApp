package com.example.levelup.model.repository

import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.data.UsuariosDao
import com.example.levelup.remote.UsuariosApiService
import com.example.levelup.remote.mappers.toDTO
import com.example.levelup.remote.mappers.toEntity
import kotlinx.coroutines.flow.Flow

class UsuariosRepository(
    private val dao: UsuariosDao,
    private val api: UsuariosApiService
) {

    // ROOM
    fun obtenerUsuarios(): Flow<List<UsuarioEntity>> = dao.obtenerUsuarios()
    fun usuarioPorId(id: Int) = dao.usuarioPorId(id)

    suspend fun insertar(usuario: UsuarioEntity) = dao.insertar(usuario)
    suspend fun actualizar(usuario: UsuarioEntity) = dao.actualizar(usuario)
    suspend fun eliminar(usuario: UsuarioEntity) = dao.eliminar(usuario)

    suspend fun login(correo: String, contrasena: String): UsuarioEntity? =
        dao.login(correo, contrasena)


    // BACKEND
    suspend fun crearUsuarioBackend(usuario: UsuarioEntity): UsuarioEntity? {
        return try {
            val dto = api.crearUsuario(usuario.toDTO())
            val entity = dto.toEntity()
            dao.insertar(entity)
            entity
        } catch (e: Exception) {
            null
        }
    }

    suspend fun actualizarUsuarioBackend(usuario: UsuarioEntity) {
        try {
            usuario.backendId?.let {
                val dto = api.actualizarUsuario(it, usuario.toDTO())
                dao.actualizar(dto.toEntity())
            }
        } catch (_: Exception) {}
    }

    suspend fun eliminarUsuarioBackend(usuario: UsuarioEntity) {
        try {
            usuario.backendId?.let { api.eliminarUsuario(it) }
            dao.eliminar(usuario)
        } catch (_: Exception) {}
    }

    suspend fun sincronizarUsuarios() {
        try {
            val remotos = api.obtenerUsuarios()
            val entidades = remotos.map { it.toEntity() }
            dao.eliminarTodos()
            dao.insertarUsuarios(entidades)
        } catch (_: Exception) {}
    }
}
