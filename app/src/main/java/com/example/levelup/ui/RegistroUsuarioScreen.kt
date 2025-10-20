package com.example.levelup.ui

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelup.viewmodel.RegistroUsuarioViewModel
import com.example.levelup.viewmodel.RegistroUsuarioViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun RegistroUsuarioScreen(
    navController: NavController
) {
    val app = LocalContext.current.applicationContext as? Application
        ?: throw IllegalStateException("No se pudo obtener la instancia de Application")

    // Inicializa el ViewModel con el Factory correcto
    val vm: RegistroUsuarioViewModel = viewModel(
        factory = RegistroUsuarioViewModelFactory(app)
    )

    // Scope para lanzar coroutines (necesario para llamar funciones suspend)
    val scope = rememberCoroutineScope()

    // Usa tu formulario principal (FormScreen)
    FormScreen(
        vm = vm,
        onSaved = {
            // Ejecuta el registro del usuario
            vm.registrarUsuario {
                // Luego del registro, verificamos el rol
                scope.launch {
                    val usuario = vm.obtenerUltimoUsuarioRegistrado()

                    if (usuario?.rol == "admin") {
                        // 🔧 Si es admin, lo lleva al panel de administración
                        navController.navigate("admin_panel") {
                            popUpTo("registro") { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        // 👤 Si es usuario normal, va al index
                        navController.navigate("index") {
                            popUpTo("registro") { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }
        }
    )
}
