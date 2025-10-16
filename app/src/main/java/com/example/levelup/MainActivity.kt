package com.example.levelup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.levelup.ui.LevelUpNavHost
import com.example.levelup.local.AppDatabase
import com.example.levelup.local.ProductosEntity
import com.example.levelup.theme.LevelUpTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pre-popular la base de datos
        prepopulateDatabase()

        setContent {
            LevelUpTheme {
                LevelUpNavHost(modifier = Modifier.fillMaxSize())
            }
        }
    }

    private fun prepopulateDatabase() {
        val db = AppDatabase.getInstance(this)
        val dao = db.productosDao()

        CoroutineScope(Dispatchers.IO).launch {
            val productos = dao.getAllProductos().first()
            if (productos.isEmpty()) {
                dao.insertProducto(
                    ProductosEntity(
                        nombre = "Mouse Gamer",
                        precio = 49990.0,
                        imagenUrl = "https://triacs.cl/86-superlarge_default_2x/mouse-gamer-led-rgb.jpg",
                        descripcion = "Mouse RGB con alta precisión"
                    )
                )
                dao.insertProducto(
                    ProductosEntity(
                        nombre = "Teclado Mecánico",
                        precio = 79990.0,
                        imagenUrl = "https://www.chilegatillos.cl/cdn/shop/files/TecladoRKRoyalKludgeR65chilegatillos.cl.jpg?v=1717042966&width=2048",
                        descripcion = "Teclado retroiluminado mecánico"
                    )
                )
            }
        }
    }
}
