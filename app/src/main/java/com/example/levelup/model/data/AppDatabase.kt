package com.example.levelup.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductosEntity::class,
        UsuarioEntity::class,
        CarritoEntity::class,
        BoletaEntity::class
    ],
    version = 13,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuariosDao
    abstract fun productosDao(): ProductosDao
    abstract fun carritoDao(): CarritoDao

    abstract fun boletaDao(): BoletasDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "levelup.db"
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getInstance(context)
                            val dao = database.productosDao()

                            val productosIniciales = listOf(
                                ProductosEntity(
                                    nombre = "Teclado Gamer",
                                    precio = 12990.0,
                                    descripcion = "Teclado ideal para gamers",
                                    imagenUrl = "https://mutant.cl/cdn/shop/files/Teclado-Gamer-Tecware-Phantom-L-Red-Switch-Mutant-27972418273349.png?v=1755035408"
                                ),
                                ProductosEntity(
                                    nombre = "Mouse Gamer",
                                    precio = 39990.0,
                                    descripcion = "Mouse cómodo ideal para tus juegos",
                                    imagenUrl = "https://s3.amazonaws.com/w3.assets/fotos/27719/1..webp?v=1883517885"
                                ),
                                ProductosEntity(
                                    nombre = "Camiseta personalizada",
                                    precio = 7990.0,
                                    descripcion = "Elige el diseño que quieras",
                                    imagenUrl = "https://iglboards.cl/cdn/shop/files/PoleraNegraIglNinoReverso.jpg?v=1722515395&width=1445"
                                )
                            )
                            dao.insertarProductos(productosIniciales)
                            println(" Productos iniciales insertados correctamente en Room")
                        }
                    }
                })
                .build()
        }
    }
}
