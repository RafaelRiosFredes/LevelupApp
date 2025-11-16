package com.example.levelup.model.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuariosDao {

    // ========== CRUD LOCAL  ===============


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: UsuarioEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuarios(lista: List<UsuarioEntity>)

    @Update
    suspend fun actualizar(usuario: UsuarioEntity)

    @Delete
    suspend fun eliminar(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    fun obtenerUsuarios(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    fun usuarioPorId(id: Int): Flow<UsuarioEntity?>

    @Query("DELETE FROM usuarios")
    suspend fun eliminarTodos()



    // ========== INTEGRACIÓN BACKEND =============

    // Buscar usuario guardado en Room por ID DEL BACKEND
    @Query("SELECT * FROM usuarios WHERE backendId = :backendId LIMIT 1")
    suspend fun usuarioPorBackendId(backendId: Long): UsuarioEntity?


    // =============== LOGIN LOCAL ================


    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun login(correo: String, contrasena: String): UsuarioEntity?
}
