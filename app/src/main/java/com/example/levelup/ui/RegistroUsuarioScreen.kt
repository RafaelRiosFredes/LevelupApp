package com.example.levelup.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelup.viewmodel.RegistroUsuarioViewModel
import com.example.levelup.viewmodel.RegistroUsuarioViewModelFactory

@Composable
fun RegistroUsuarioScreen(
    navController: NavController
) {
    // ✅ Cast seguro para evitar crashes si el contexto no es Application
    val app = LocalContext.current.applicationContext as? Application
        ?: throw IllegalStateException("No se pudo obtener la instancia de Application")

    val vm: RegistroUsuarioViewModel = viewModel(
        factory = RegistroUsuarioViewModelFactory(app)
    )

    // ✅ Llamamos al formulario y conectamos la navegación
    FormScreen(
        vm = vm,
        onBack = { navController.popBackStack() },
        onSaved = {
            vm.registrarUsuario {
                // Al registrarse correctamente, navegar a login
                navController.navigate("login") {
                    popUpTo("registro") { inclusive = true }
                }
            }
        }
    )
}
