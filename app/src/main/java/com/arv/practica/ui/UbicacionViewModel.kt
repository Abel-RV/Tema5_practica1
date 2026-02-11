package com.arv.practica.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arv.practica.data.AppDatabase
import com.arv.practica.data.PuntoInteresRepository
import com.arv.practica.models.PuntoInteres
import com.arv.practica.models.Ubicacion
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class UbicacionViewModel(application: Application): AndroidViewModel(application) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    private val _ubicacion = MutableStateFlow<Ubicacion?>(null)
    val ubicacion: StateFlow<Ubicacion?> = _ubicacion

    private val _direccion = MutableStateFlow<String>("")
    val direccion: StateFlow<String> = _direccion

    private val db by lazy { AppDatabase.getDatabase(getApplication()) }
    private val repository by lazy { PuntoInteresRepository(db.puntoInteresDao()) }
    private val _puntosGuardados = MutableStateFlow<List<PuntoInteres>>(emptyList())
    val puntosGuardados: StateFlow<List<PuntoInteres>> = _puntosGuardados

    init {
        viewModelScope.launch {
            repository.obtenerTodos().collect { puntos->
                _puntosGuardados.value=puntos
            }
        }
    }

    fun obtenerDireccion(lat: Double,Ing: Double){
        viewModelScope.launch {
            try{
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                val addresses= withContext(Dispatchers.IO) {
                    geocoder.getFromLocation(lat,Ing,1)
                }
                _direccion.value= addresses?.firstOrNull()?.getAddressLine(0)?:"Ubicacion desconocida"
            }catch (e: Exception){
                _direccion.value="Error al obtener"
            }
        }
    }


    fun obtenerUbicacionActual(){
        if(ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location?->
                location?.let {
                    val ubi = Ubicacion(it.latitude,it.longitude)
                    _ubicacion.value=ubi
                    obtenerDireccion(ubi.lat,ubi.ing)
                }
            }
        }
    }

    fun guardarPunto(nombre:String,ubicacion: com.arv.practica.models.Ubicacion,direccion:String){
        viewModelScope.launch {
            val punto = PuntoInteres(
                nombre=nombre,
                ubicacion = ubicacion,
                direccion = direccion
            )
            repository.guardar(punto)
        }
    }
}