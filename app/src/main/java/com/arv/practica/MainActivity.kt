package com.arv.practica

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // <--- IMPORTANTE
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arv.practica.ui.MapaUbicacion
import com.arv.practica.ui.SensorViewModel
import com.arv.practica.ui.UbicacionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RutaPersonalApp()
        }
    }
}
@Composable
fun RutaPersonalApp() {
    val sensorVm: SensorViewModel = viewModel()
    val ubicacionVm: UbicacionViewModel = viewModel()
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) ubicacionVm.obtenerUbicacionActual()
        }
    )
    var nombrePunto by remember { mutableStateOf("") }

    // Recogemos los puntos guardados del ViewModel
    val listaPuntos = ubicacionVm.puntosGuardados.collectAsState().value

    LaunchedEffect(Unit) {
        sensorVm.iniciarSensor(LocalContext.current)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text("Aceleración X: ${sensorVm.aceleracionX.collectAsState().value}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }) {
            Text("Obtener ubicación")
        }
        Spacer(modifier = Modifier.height(16.dp))

        ubicacionVm.ubicacion.collectAsState().value?.let { ubicacionActual ->
            Text("Ubicación: ${ubicacionActual.lat}, ${ubicacionActual.ing}")
            Text("Dirección: ${ubicacionVm.direccion.collectAsState().value}")
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombrePunto,
                onValueChange = { nombrePunto = it },
                label = { Text("Nombre del punto") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (nombrePunto.isNotBlank()) {
                    ubicacionVm.guardarPunto(
                        nombre = nombrePunto,
                        ubicacion = ubicacionActual,
                        direccion = ubicacionVm.direccion.collectAsState().value
                    )
                    nombrePunto = ""
                }
            }) {
                Text("Guardar punto")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Preparamos la lista de ubicaciones para el mapa
            val puntosComoUbicacion = listaPuntos.map { it.ubicacion }

            // Llamamos al mapa (Sin el error 19 y con los tipos correctos)
            MapaUbicacion(
                ubicacion = ubicacionActual,
                puntosGuardados = puntosComoUbicacion,
                context = LocalContext.current
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Puntos guardados:", style = MaterialTheme.typography.titleMedium)

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                // Usamos 'listaPuntos' que definimos arriba
                items(listaPuntos) { punto ->
                    Text(". ${punto.nombre} – ${punto.direccion}")
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}