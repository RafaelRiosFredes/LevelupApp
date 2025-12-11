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
        CarritoEntity::class,
        BoletaEntity::class,
        OpinionEntity::class
    ],
    version = 18,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productosDao(): ProductosDao
    abstract fun carritoDao(): CarritoDao
    abstract fun boletaDao(): BoletasDao
    abstract fun opinionesDao(): OpinionesDao


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

                            // ================================
                            //   PRODUCTOS INICIALES DEMO
                            // ================================
                            val productosIniciales = listOf(
                                ProductosEntity(
                                    id = 0L,
                                    backendId = null,
                                    nombre = "Teclado Gamer",
                                    descripcion = "Teclado ideal para gamers",
                                    precio = 12990,
                                    stock = 15,
                                    imagenUrl = "https://mutant.cl/cdn/shop/files/Teclado-Gamer-Tecware-Phantom-L-Red-Switch-Mutant-27972418273349.png?v=1755035408",
                                    categoriaId = 1L,
                                    categoriaNombre = "Accesorios"
                                ),
                                ProductosEntity(
                                    id = 0L,
                                    backendId = null,
                                    nombre = "Mouse Gamer",
                                    descripcion = "Mouse cómodo ideal para juegos",
                                    precio = 39990,
                                    stock = 20,
                                    imagenUrl = "https://s3.amazonaws.com/w3.assets/fotos/27719/1..webp?v=1883517885",
                                    categoriaId = 1L,
                                    categoriaNombre = "Accesorios"
                                ),
                                ProductosEntity(
                                    id = 0L,
                                    backendId = null,
                                    nombre = "Camiseta personalizada",
                                    descripcion = "Diseño a elección",
                                    precio = 7990,
                                    stock = 50,
                                    imagenUrl = "https://iglboards.cl/cdn/shop/files/PoleraNegraIglNinoReverso.jpg?v=1722515395&width=1445",
                                    categoriaId = 2L,
                                    categoriaNombre = "Ropa"
                                )
                            )

                            dao.insertarProductos(productosIniciales)

                            println("✔ Productos iniciales insertados en ROOM")
                        }
                    }
                })
                .build()
        }
    }
}
