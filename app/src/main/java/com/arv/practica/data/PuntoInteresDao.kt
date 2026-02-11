package com.arv.practica.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arv.practica.models.PuntoInteres
import kotlinx.coroutines.flow.Flow

@Dao
interface PuntoInteresDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(punto: PuntoInteres)

    @Query("SELECT * FROM puntointeres ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<PuntoInteres>>

    @Delete
    suspend fun eliminar(punto: PuntoInteres)
}