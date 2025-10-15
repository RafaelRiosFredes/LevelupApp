package com.example.levelup.ui


import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelup.viewmodel.RegistroUsuarioViewModel
import com.example.levelup.viewmodel.RegistroUsuarioViewModelFactory

@Composable
fun RegistroUsuarioScreen(
    onNavigateToLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val vm: RegistroUsuarioViewModel = viewModel(
        factory = RegistroUsuarioViewModelFactory(context.applicationContext as Application)
    )

    FormScreen(
        vm = vm,
        onBack = onNavigateToLogin,
        onSaved = { vm.registrarUsuario(onSuccess = onNavigateToLogin) }
    )
}
