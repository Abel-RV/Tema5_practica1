package com.arv.practica.data

import com.arv.practica.models.PuntoInteres

class PuntoInteresRepository(private val dao: PuntoInteresDao) {
    fun obtenerTodos()= dao.obtenerTodos()
    suspend fun guardar(punto: PuntoInteres)= dao.insertar(punto)
}