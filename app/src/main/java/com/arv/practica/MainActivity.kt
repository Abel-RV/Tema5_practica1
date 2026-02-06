package com.arv.practica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arv.practica.ui.SensorViewModel
import com.arv.practica.ui.UbicacionViewModel
import com.arv.practica.ui.theme.Tema5_practica1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
        }
    }
}

@Composable
fun RutaPersonalApp(){
    val sensorVm: SensorViewModel= viewModel()
    val ubicacionVm: UbicacionViewModel=viewModel()
    val context = LocalContext.current
    LaunchedEffect(Unit) {sensorVm.iniciarSensor(context) }

    val permissionLauncher

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Aceleracion X: ${sensorVm.aceleracionX.collectAsState().value}")
        Text("Aceleracion X: ${String.format("%.2f",sensorVm.aceleracionX.collectAsState().value)}")

    }
}