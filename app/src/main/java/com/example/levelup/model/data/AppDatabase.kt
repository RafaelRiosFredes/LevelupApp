package com.example.levelup.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.levelup.R
import  com.example.levelup.model.data.CarritoEntity
import com.example.levelup.model.data.CarritoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductosEntity::class,
        UsuarioEntity::class,
        CarritoEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun productosDao(): ProductosDao
    abstract fun carritoDao(): CarritoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "levelup.db"
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        // Inserción inicial en background
                        CoroutineScope(Dispatchers.IO).launch {
                            // Espera a que INSTANCE esté asignado (ya que buildDatabase devuelve el instance y lo asignamos con also)
                            val dao = INSTANCE?.productosDao() ?: return@launch
                            val productosIniciales = listOf(

                                ProductosEntity(
                                    nombre = "Teclado Gamer",
                                    precio = 12990.0,
                                    descripcion = "Teclado ideal para gamers",
                                    imagenRes = R.drawable.ic_launcher_foreground
                                ),
                                ProductosEntity(
                                    nombre = "Mouse Gamer",
                                    precio = 39990.0,
                                    descripcion = "Mouse cómodo ideal para tus juegos",
                                    imagenRes = R.drawable.ic_launcher_foreground
                                ),
                                ProductosEntity(
                                    nombre = "Camiseta personalizada",
                                    precio = 7990.0,
                                    descripcion = "Elige el diseño que quieras",
                                    imagenRes = R.drawable.ic_launcher_foreground
                                )
                            )
                            dao.insertarProductos(productosIniciales)
                        }
                    }
                })
                .build()
            return instance
        }
    }
}
