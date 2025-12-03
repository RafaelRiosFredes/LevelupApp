package com.example.levelup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.levelup.core.UserSession
import com.example.levelup.ui.components.DrawerGlobal
import com.example.levelup.ui.theme.GamerGreen
import com.example.levelup.ui.theme.JetBlack
import com.example.levelup.ui.theme.PureWhite
import com.example.levelup.viewmodel.BoletaViewModel
import com.example.levelup.viewmodel.CarritoViewModel
import kotlinx.coroutines.launch

// mismas regiones que ya tienes
val regionesConComunas = mapOf(
    "Arica y Parinacota" to listOf("Arica", "Camarones", "Putre", "General Lagos"),
    "Tarapacá" to listOf("Iquique", "Alto Hospicio", "Pozo Almonte", "Camiña", "Colchane", "Huara", "Pica"),
    "Antofagasta" to listOf("Antofagasta", "Mejillones", "Sierra Gorda", "Taltal", "Calama", "Ollagüe", "San Pedro de Atacama", "Tocopilla", "María Elena"),
    "Atacama" to listOf("Copiapó", "Caldera", "Tierra Amarilla", "Chañaral", "Diego de Almagro", "Vallenar", "Alto del Carmen", "Freirina", "Huasco"),
    "Coquimbo" to listOf("La Serena", "Coquimbo", "Andacollo", "La Higuera", "Paiguano", "Vicuña", "Illapel", "Canela", "Los Vilos", "Salamanca", "Ovalle", "Combarbalá", "Monte Patria", "Punitaqui", "Río Hurtado"),
    "Valparaíso" to listOf("Valparaíso", "Casablanca", "Concón", "Juan Fernández", "Puchuncaví", "Quintero", "Viña del Mar", "Isla de Pascua", "Los Andes", "Calle Larga", "Rinconada", "San Esteban", "La Ligua", "Cabildo", "Papudo", "Petorca", "Zapallar", "Quillota", "Calera", "Hijuelas", "La Cruz", "Nogales", "San Antonio", "Algarrobo", "Cartagena", "El Quisco", "El Tabo", "Santo Domingo", "San Felipe", "Catemu", "Llaillay", "Panquehue", "Putaendo", "Santa María", "Quilpué", "Limache", "Olmué", "Villa Alemana"),
    "Metropolitana de Santiago" to listOf("Cerrillos", "Cerro Navia", "Conchalí", "El Bosque", "Estación Central", "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja", "La Pintana", "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipú", "Ñuñoa", "Pedro Aguirre Cerda", "Peñalolén", "Providencia", "Pudahuel", "Quilicura", "Quinta Normal", "Recoleta", "Renca", "San Joaquín", "San Miguel", "San Ramón", "Santiago", "Vitacura", "Puente Alto", "Pirque", "San José de Maipo", "Colina", "Lampa", "Tiltil", "San Bernardo", "Buin", "Calera de Tango", "Paine", "Melipilla", "Alhué", "Curacaví", "María Pinto", "San Pedro", "Talagante", "El Monte", "Isla de Maipo", "Padre Hurtado", "Peñaflor"),
    "Biobío" to listOf("Concepción", "Coronel", "Chiguayante", "Florida", "Hualqui", "Lota", "Penco", "San Pedro de la Paz", "Santa Juana", "Talcahuano", "Tomé", "Hualpén", "Lebu", "Arauco", "Cañete", "Contulmo", "Curanilahue", "Los Álamos", "Tirúa", "Los Ángeles", "Antuco", "Cabrero", "Laja", "Mulchén", "Nacimiento", "Negrete", "Quilaco", "Quilleco", "San Rosendo", "Santa Bárbara", "Tucapel", "Yumbel", "Alto Biobío")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCompraScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel,
    boletaViewModel: BoletaViewModel,
    totalFinal: Long,
    descuentoAplicado: Int
) {
    val scope = rememberCoroutineScope()
    val carrito by carritoViewModel.carrito.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var nombre by remember { mutableStateOf(UserSession.nombre ?: "") }
    var apellidos by remember { mutableStateOf(UserSession.apellidos ?: "") }
    var correo by remember { mutableStateOf(UserSession.correo ?: "") }

    var regionSeleccionada by remember { mutableStateOf("") }
    var comunaSeleccionada by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var tarjeta by remember { mutableStateOf("") }

    var regionExpanded by remember { mutableStateOf(false) }
    var comunaExpanded by remember { mutableStateOf(false) }

    DrawerGlobal(navController = navController) {
        Scaffold(
            containerColor = JetBlack,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Detalle de Compra", color = GamerGreen) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = GamerGreen)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = JetBlack)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Resumen
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Resumen del Pedido", color = GamerGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Total a Pagar: $${totalFinal}", color = PureWhite, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        if (descuentoAplicado > 0) {
                            Text("Descuento aplicado: $descuentoAplicado%", color = GamerGreen, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Datos cliente
                Text("Información del Cliente", color = GamerGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    colors = gamerInputColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos") },
                    colors = gamerInputColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    colors = gamerInputColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Dirección
                Text("Dirección de Despacho", color = GamerGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = regionExpanded,
                    onExpandedChange = { regionExpanded = !regionExpanded }
                ) {
                    OutlinedTextField(
                        value = regionSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Región") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                        colors = gamerInputColors(),
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = regionExpanded,
                        onDismissRequest = { regionExpanded = false }
                    ) {
                        regionesConComunas.keys.forEach { region ->
                            DropdownMenuItem(
                                text = { Text(text = region) },
                                onClick = {
                                    regionSeleccionada = region
                                    comunaSeleccionada = ""
                                    regionExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = comunaExpanded,
                    onExpandedChange = { comunaExpanded = !comunaExpanded }
                ) {
                    OutlinedTextField(
                        value = comunaSeleccionada,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Comuna") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunaExpanded) },
                        colors = gamerInputColors(),
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        enabled = regionSeleccionada.isNotEmpty()
                    )
                    ExposedDropdownMenu(
                        expanded = comunaExpanded,
                        onDismissRequest = { comunaExpanded = false }
                    ) {
                        val comunas = regionesConComunas[regionSeleccionada] ?: emptyList()
                        comunas.forEach { comuna ->
                            DropdownMenuItem(
                                text = { Text(text = comuna) },
                                onClick = {
                                    comunaSeleccionada = comuna
                                    comunaExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = calle,
                        onValueChange = { calle = it },
                        label = { Text("Calle") },
                        colors = gamerInputColors(),
                        modifier = Modifier.weight(0.7f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = numero,
                        onValueChange = { numero = it },
                        label = { Text("N°") },
                        colors = gamerInputColors(),
                        modifier = Modifier.weight(0.3f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Pago
                Text("Método de Pago", color = GamerGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tarjeta,
                    onValueChange = { if (it.length <= 16) tarjeta = it },
                    label = { Text("Número de Tarjeta (16 dígitos)") },
                    colors = gamerInputColors(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Pagar
                Button(
                    onClick = {

                        if (UserSession.jwt.isNullOrBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Debes iniciar sesión para pagar")
                            }
                            navController.navigate("login")
                            return@Button
                        }

                        if (nombre.isBlank() || calle.isBlank() || regionSeleccionada.isBlank()) {
                            scope.launch { snackbarHostState.showSnackbar("Completa los datos de envío") }
                            return@Button
                        }
                        if (tarjeta.length != 16) {
                            scope.launch { snackbarHostState.showSnackbar("Tarjeta inválida (debe tener 16 dígitos)") }
                            return@Button
                        }
                        if (carrito.isEmpty()) {
                            scope.launch { snackbarHostState.showSnackbar("Tu carrito está vacío") }
                            return@Button
                        }

                        scope.launch {
                            try {
                                val boletaRemota = boletaViewModel.crearBoletaBackend(
                                    itemsCarrito = carrito,
                                    totalFinal = totalFinal,
                                    descuentoAplicado = descuentoAplicado
                                )

                                carritoViewModel.vaciarCarrito()

                                snackbarHostState.showSnackbar(
                                    "¡Compra exitosa! Boleta #${boletaRemota.idBoleta}"
                                )

                                navController.navigate("boleta_detalle/${boletaRemota.idBoleta}") {
                                    popUpTo("carrito") { inclusive = true }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()

                                val msg = if (e is retrofit2.HttpException) {
                                    val errorBody = e.response()?.errorBody()?.string()
                                    "Error HTTP ${e.code()} -> $errorBody"
                                } else {
                                    e.message ?: "Error desconocido"
                                }

                                snackbarHostState.showSnackbar("Error al procesar compra: $msg")
                            }

                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GamerGreen,
                        contentColor = JetBlack
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("PAGAR $$totalFinal", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun gamerInputColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GamerGreen,
    unfocusedBorderColor = Color.Gray,
    focusedTextColor = PureWhite,
    unfocusedTextColor = PureWhite,
    focusedLabelColor = GamerGreen,
    unfocusedLabelColor = Color.Gray,
    cursorColor = GamerGreen
)
