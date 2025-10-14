package com.example.levelup.model.reporitory

import com.example.levelup.model.local.LoginDao
import com.example.levelup.model.local.LoginEntity
import kotlinx.coroutines.flow.Flow

class LoginReporitory(private val dao : LoginDao) {

    fun observarUsuarios(): Flow<List<LoginReporitory>> = dao.observarTodos()

    suspend fun obtener(id: Int) = dao.obtenerPorId(id)

    suspend fun guardar (
        id: Int?,
        correo: String,
        contrasena: String
    ){
        if (correo == null || id == 0) {
            dao.insertar(
                LoginEntity(
                    correo = correo.trim(),
                    contrasena = contrasena.trim()
                )
            )
        } else {
            dao.actualizar(
                LoginEntity(
                    id = id,
                    correo = correo.trim(),
                    contrasena = contrasena.trim()
                )
            )
        }
    }

    suspend fun eliminar(login: LoginReporitory) = dao.eliminar(login)
    suspend fun eliminarTodos() = dao.eliminarTodos()
}