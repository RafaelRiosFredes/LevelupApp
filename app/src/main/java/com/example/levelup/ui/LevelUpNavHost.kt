package com.example.levelup.ui



import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

import androidx.navigation.compose.NavHost

import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController





/**

 * NavHost del módulo Productos.

 * Ahora la pantalla principal es ProductosScreen.

 * Además ya incluye rutas para las demás pantallas de otras ramas (placeholders).

 */

@Composable

fun LevelUpNavHost() {
    val nav = rememberNavController()


    NavHost(
        navController = nav,
        startDestination = "productos" // ✅ arranca desde ProductosScreen

    ) {
        // 🛒 Pantalla de productos (principal de esta rama)

        composable("productos") {
            ProductosScreen(
                onNavigateBack = { nav.popBackStack() }

            )

        }

        composable("producto/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            if (id != null) {
                ProductoScreen(id = id, onNavigateBack = { nav.popBackStack() })
            }
        }

        // 🔐 Login (placeholder temporal)

        composable("login") {
            PlaceholderScreen("Pantalla de Login (en desarrollo)")

        }



        // 📝 Registro de Usuario (placeholder temporal)

        composable("registro") {
            PlaceholderScreen("Pantalla de Registro de Usuario (en desarrollo)")

        }



        // 📰 Noticias (placeholder temporal)

        composable("noticias") {
            PlaceholderScreen("Pantalla de Noticias (en desarrollo)")

        }

    }

}



/**

 * Pantalla temporal que muestra texto cuando una ruta aún no está lista.

 */

@Composable

fun PlaceholderScreen(texto: String) {

    androidx.compose.material3.Surface {
        androidx.compose.foundation.layout.Column(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(32.dp),

            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally

        ) {

            androidx.compose.material3.Text(texto)

        }

    }

}