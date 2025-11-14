package com.example.levelup.TestUsuario

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.data.UsuarioEntity
import com.example.levelup.model.repository.UsuariosRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UsuariosRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var repo: UsuariosRepository

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        repo = UsuariosRepository(db.usuarioDao())
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertarUsuario() = runBlocking {
        val u = UsuarioEntity(
            nombres = "Pedro",
            apellidos = "Lopez",
            correo = "p@mail.com",
            contrasena = "123456",
            telefono = 0,
            fechaNacimiento = "01/01/2000",
            fotoPerfil = null
        )

        repo.insertar(u)

        val lista = repo.todosLosUsuarios().first()
        Assert.assertEquals(1, lista.size)
    }

    @Test
    fun loginCorrecto() = runBlocking {
        val correo = "login@mail.com"
        val pass = "111111"

        repo.insertar(
            UsuarioEntity(
                nombres = "Login",
                apellidos = "OK",
                correo = correo,
                contrasena = pass,
                telefono = 0,
                fechaNacimiento = "01/01/2000",
                fotoPerfil = null
            )
        )

        val user = repo.login(correo, pass)
        Assert.assertNotNull(user)
        Assert.assertEquals("Login", user?.nombres)
    }
}