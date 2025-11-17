package com.example.levelup.model.repository

import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.data.UsuariosDao
import com.example.levelup.remote.AuthApiService
import com.example.levelup.remote.RegistroUsuarioRemoteDTO
import com.example.levelup.remote.UsuariosApiService
import com.example.levelup.remote.mappers.toDTO
import com.example.levelup.remote.mappers.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UsuariosRepository(
    private val dao: UsuariosDao,
    private val api: UsuariosApiService,
    private val authApi: AuthApiService
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

    suspend fun registrarUsuarioBackendYLocal(
        nombres: String,
        apellidos: String,
        correo: String,
        contrasena: String,
        telefono: Long?,
        fechaNacimiento: String,
        fotoPerfilBase64: String?,
        duoc: Boolean,
        descApl: Boolean,
        rol: String
    ): Result<UsuarioEntity> = withContext(Dispatchers.IO) {
        try {
            // 1. DTO remoto
            val tel = telefono ?: 0L  // si tu backend exige no-nulo
            val dtoRemoto = RegistroUsuarioRemoteDTO(
                nombres = nombres,
                apellidos = apellidos,
                correo = correo,
                contrasena = contrasena,
                telefono = tel,
                fechaNacimiento = fechaNacimiento   // "YYYY-MM-DD"
            )

            // 2. Llamada al backend
            val resp = authApi.registrar(dtoRemoto)

            // 3. Mapear respuesta a tu entity local
            val entity = UsuarioEntity(
                id = resp.idUsuario.toInt(), // si tu entity usa Int
                nombres = resp.nombres,
                apellidos = resp.apellidos,
                correo = resp.correo,
                contrasena = contrasena, // NO viene en la respuesta
                telefono = resp.telefono,
                fechaNacimiento = resp.fechaNacimiento,
                // dejamos lógica local para esto:
                fotoPerfil = fotoPerfilBase64,
                duoc = duoc,
                descApl = descApl,
                rol = rol
            )

            dao.insertar(entity)

            Result.success(entity)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
}
