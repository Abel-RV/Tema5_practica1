package com.arv.practica.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PuntoInteres(
    @PrimaryKey val id: Long= System.currentTimeMillis(),
    val nombre: String,
    @Embedded val ubicacion: Ubicacion,
    val direccion: String
)
