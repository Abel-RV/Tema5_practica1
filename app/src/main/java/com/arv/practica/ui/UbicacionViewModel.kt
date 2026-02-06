package com.arv.practica.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Ubicacion(val lat: Double,val Ing: Double)

class UbicacionViewModel(application: Application): AndroidViewModel(application) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val _ubicacion = MutableStateFlow<Ubicacion?>(null)
    val ubicacion: StateFlow<Ubicacion?> = _ubicacion

    fun obtenerUbicacionActual(){
        if(ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location?->
                location?.let {
                    _ubicacion.value= Ubicacion(it.latitude,it.longitude)
                }
            }
        }
    }
}