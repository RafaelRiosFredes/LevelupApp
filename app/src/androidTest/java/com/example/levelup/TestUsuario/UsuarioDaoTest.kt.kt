package com.example.levelup.TestUsuario

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.levelup.model.data.AppDatabase
import com.example.levelup.model.data.UsuarioDao
import com.example.levelup.model.data.UsuarioEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*

class UsuarioDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: UsuarioDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = db.usuarioDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertarYObtenerUsuarios() = runBlocking {
        val u = UsuarioEntity(
            nombres = "Juan",
            apellidos = "Test",
            correo = "j@test.com",
            contrasena = "123456",
            telefono = 1234,
            fechaNacimiento = "01/01/2000",
            fotoPerfil = null
        )

        dao.insertarUsuario(u)

        val lista = dao.obtenerTodos().first()
        Assert.assertEquals(1, lista.size)
        Assert.assertEquals("Juan", lista[0].nombres)
    }

    @Test
    fun loginCorrecto() = runBlocking {
        val u = UsuarioEntity(
            nombres = "Login",
            apellidos = "OK",
            correo = "login@mail.com",
            contrasena = "123456",
            telefono = 1,
            fechaNacimiento = "01/01/2000",
            fotoPerfil = null
        )

        dao.insertarUsuario(u)

        val encontrado = dao.verificarLogin("login@mail.com", "123456")
        Assert.assertNotNull(encontrado)
        Assert.assertEquals("Login", encontrado?.nombres)
    }
}
