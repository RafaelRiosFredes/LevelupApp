@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.levelup.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Map // Icono de mapa
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
import com.example.levelup.ui.components.CompraChecklistDialog
import com.example.levelup.remote.BoletaRemoteDTO

// Datos de regiones
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
    val context = LocalContext.current

    // --- ESTADOS DE FORMULARIO ---
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

    // --- ESTADOS PARA LA ANIMACIÓN ---
    var showAnimation by remember { mutableStateOf(false) }
    var compraExitosaDto by remember { mutableStateOf<BoletaRemoteDTO?>(null) }
    var errorCompra by remember { mutableStateOf<String?>(null) }

    // LOGICA DEL DIALOGO DE ANIMACIÓN
    if (showAnimation) {
        CompraChecklistDialog(
            onDismiss = { /* No dejar cerrar */ },
            onAnimationFinished = {
                if (compraExitosaDto != null) {
                    carritoViewModel.vaciarCarrito()
                    navController.navigate("boleta_detalle/${compraExitosaDto!!.idBoleta}") {
                        popUpTo("carrito") { inclusive = true }
                    }
                } else if (errorCompra != null) {
                    showAnimation = false
                    scope.launch { snackbarHostState.showSnackbar(errorCompra ?: "Error desconocido") }
                }
            }
        )
    }

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

                Spacer(modifier = Modifier.height(12.dp))

                // --- RECURSO NATIVO: BOTÓN ABRIR MAPAS ---
                OutlinedButton(
                    onClick = {
                        // Construimos la dirección para la búsqueda
                        val direccionCompleta = "$calle $numero, $comunaSeleccionada, $regionSeleccionada, Chile"

                        // Si no ha escrito nada, abrimos el mapa general
                        val query = if (calle.isNotBlank() || comunaSeleccionada.isNotBlank())
                            "geo:0,0?q=${Uri.encode(direccionCompleta)}"
                        else
                            "geo:0,0"

                        // Lanzamos el Intent Nativo
                        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(query))
                        // Intentamos abrir Google Maps específicamente si está instalado, sino cualquiera
                        mapIntent.setPackage("com.google.android.apps.maps")

                        try {
                            context.startActivity(mapIntent)
                        } catch (e: Exception) {
                            // Si no tiene Google Maps, intentamos abrir cualquier mapa
                            val genericMapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(query))
                            try {
                                context.startActivity(genericMapIntent)
                            } catch (ex: Exception) {
                                Toast.makeText(context, "No tienes app de mapas instalada", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = GamerGreen
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GamerGreen)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Ver dirección en Mapa")
                }
                // ------------------------------------------

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

                // --- BOTÓN PAGAR ---
                Button(
                    onClick = {
                        // VALIDACIONES
                        if (UserSession.jwt.isNullOrBlank()) {
                            scope.launch { snackbarHostState.showSnackbar("Debes iniciar sesión para pagar") }
                            navController.navigate("login")
                            return@Button
                        }
                        if (nombre.isBlank() || calle.isBlank() || regionSeleccionada.isBlank()) {
                            scope.launch { snackbarHostState.showSnackbar("Completa los datos de envío") }
                            return@Button
                        }
                        if (tarjeta.length != 16) {
                            scope.launch { snackbarHostState.showSnackbar("Tarjeta inválida (16 dígitos)") }
                            return@Button
                        }
                        if (carrito.isEmpty()) {
                            scope.launch { snackbarHostState.showSnackbar("Carrito vacío") }
                            return@Button
                        }

                        // INICIAR PROCESO DE COMPRA Y ANIMACIÓN
                        showAnimation = true
                        errorCompra = null
                        compraExitosaDto = null

                        scope.launch {
                            try {
                                val respuestaBackend = boletaViewModel.crearBoletaBackend(
                                    itemsCarrito = carrito,
                                    totalFinal = totalFinal,
                                    descuentoAplicado = descuentoAplicado
                                )
                                compraExitosaDto = respuestaBackend
                            } catch (e: Exception) {
                                e.printStackTrace()
                                val msg = if (e is retrofit2.HttpException) {
                                    val errorBody = e.response()?.errorBody()?.string()
                                    "Error HTTP ${e.code()} -> $errorBody"
                                } else {
                                    e.message ?: "Error desconocido"
                                }
                                errorCompra = "Error: $msg"
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