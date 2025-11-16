package com.example.levelup

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.levelup.ui.ProductoScreen
import com.example.levelup.model.data.ProductosEntity
import com.example.levelup.viewmodel.ProductosViewModel
import com.example.levelup.viewmodel.CarritoViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class ProductoScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    // 🔹 Fake ProductosViewModel (simple para test)
    class FakeProductosVM : ProductosViewModel(application = null) {
        override fun obtenerProductoPorId(id: Int): Flow<ProductosEntity?> {
            return flowOf(
                ProductosEntity(
                    id = 1,
                    nombre = "Producto Test",
                    descripcion = "Descripción test",
                    precio = 999.0,
                    imagenUrl = ""
                )
            )
        }
    }

    // 🔹 Fake CarritoViewModel
    class FakeCarritoVM : CarritoViewModel(application = null)

    @Test
    fun productoScreen_muestraNombreProducto() {

        composeRule.setContent {
            ProductoScreen(
                productosViewModel = FakeProductosVM(),
                carritoViewModel = FakeCarritoVM(),
                id = 1,
                onNavigateBack = {}
            )
        }

        composeRule
            .onNodeWithText("Producto Test")
            .assertExists()
    }
}
