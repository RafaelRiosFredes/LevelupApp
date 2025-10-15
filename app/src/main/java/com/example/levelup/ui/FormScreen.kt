package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.levelup.viewmodel.RegistroUsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    vm: RegistroUsuarioViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val form by vm.form.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Registro") }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            form.mensaje?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            OutlinedTextField(
                value = form.nombres,
                onValueChange = vm::onChangeNombres,
                label = { Text("Nombres") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.apellidos,
                onValueChange = vm::onChangeApellidos,
                label = { Text("Apellidos") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.correo,
                onValueChange = vm::onChangeCorreo,
                label = { Text("Correo electrónico") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.contrasena,
                onValueChange = vm::onChangeContrasena,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.telefono,
                onValueChange = vm::onChangeTelefono,
                label = { Text("Teléfono (opcional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.fechaNacimiento,
                onValueChange = vm::onChangeFechaNacimiento,
                label = { Text("Fecha de nacimiento (dd/MM/yyyy)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onSaved, modifier = Modifier.weight(1f)) { Text("Registrar") }
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Cancelar") }
            }
        }
    }
}
