package com.example.levelup.model.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.levelup.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ProductosEntity::class],
    version = 3, // ⚠️ Aumenta este número cuando cambies algo
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productosDao(): ProductosDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "levelup.db"
                )
                    // ✅ Esto borra automáticamente versiones antiguas con datos erróneos
                    .fallbackToDestructiveMigration()
                    // ✅ Callback para insertar productos válidos una sola vez
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).productosDao()
                                val productosIniciales = listOf(
                                    ProductosEntity(
                                        nombre = "Teclado Gamer",
                                        precio = 12990.0,
                                        descripcion = "Teclado ideal para gamers",
                                        imagenRes = R.drawable.producto1
                                    ),
                                    ProductosEntity(
                                        nombre = "Mouse Gamer",
                                        precio = 39990.0,
                                        descripcion = "Mouse cómodo ideal para tus juegos",
                                        imagenRes = R.drawable.mouse
                                    ),
                                    ProductosEntity(
                                        nombre = "Camiseta personalizada",
                                        precio = 7990.0,
                                        descripcion = "Elige el diseño que quieras",
                                        imagenRes = R.drawable.descarga
                                    )
                                )
                                dao.insertarProductos(productosIniciales)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
