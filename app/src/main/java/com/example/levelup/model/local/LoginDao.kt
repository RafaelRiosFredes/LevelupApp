package com.example.levelup.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.levelup.model.reporitory.LoginReporitory
import kotlinx.coroutines.flow.Flow


@Dao
interface LoginDao {

    @Query("SELECT * FROM usuarios")
    fun observarTodos(): Flow<List<LoginReporitory>>

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtenerPorId(id: Int): LoginEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(expense: LoginEntity): Long

    @Update
    suspend fun actualizar(expense: LoginEntity)

    @Delete
    suspend fun eliminar(expense: LoginReporitory)

    @Query(value = "DELETE FROM usuarios")
    suspend fun eliminarTodos()
}