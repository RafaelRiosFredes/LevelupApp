package com.example.levelup.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.levelup.R
import com.example.levelup.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(vm: LoginViewModel, navController: NavController) {
    val form by vm.form.collectAsState()
    val Russo_One = FontFamily(Font(R.font.russo_one))

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo LevelUp
            Image(
                painter = painterResource(id = R.drawable.levelup2),
                contentDescription = "Logo LevelUp",
                modifier = Modifier
                    .size(250.dp)
                    .padding(top = 5.dp),
                contentScale = ContentScale.Fit
            )

            // Título
            Text(
                text = "¡Hola! Inicia sesión",
                color = Color.White,
                fontSize = 22.dp.value.sp,
                fontFamily = Russo_One,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(24.dp))

            // Campo de correo
            OutlinedTextField(
                value = form.correo,
                onValueChange = vm::onChangeCorreo,
                label = { Text("Correo electrónico", color = Color.White) },
                modifier = Modifier
                    .width(350.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.Blue,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(Modifier.height(24.dp))

            //  Campo de contraseña
            OutlinedTextField(
                value = form.contrasena,
                onValueChange = vm::onChangeContrasena,
                label = { Text("Contraseña", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .width(350.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    focusedIndicatorColor = Color.Blue,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.Blue,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )


            Spacer(Modifier.height(30.dp))

            //  Boton de inicio de sesión
                Button(
                    onClick = {
                        vm.validarUsuario()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp)
                ) {
                    Text(
                        text = "Ingresar",
                        fontSize = 16.sp,
                        fontFamily = Russo_One,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

            form.mensaje?.let {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = it,
                    color = if (form.exito) Color.Green else Color.Red
                )
            }

            // Si login fue exitoso navega al index
            LaunchedEffect(form.exito) {
                if (form.exito) {
                    kotlinx.coroutines.delay(1500) // para ver el mensaje primero
                    navController.navigate("index") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            Spacer(Modifier.height(30.dp))

            Text(
                text = "¿Aún sin cuenta? Crea una aquí",
                color = Color.White,
                fontSize = 15.sp,
                fontFamily = Russo_One,
                modifier = Modifier.clickable {
                    navController.navigate("registro")
                }
            )
        }
    }
}
