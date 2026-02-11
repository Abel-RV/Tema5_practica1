package com.arv.practica.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.arv.practica.models.Ubicacion
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.CustomZoomButtonsController

@Composable
fun MapaUbicacion(
    ubicacion: Ubicacion?,
    puntosGuardados: List<Ubicacion>,
    context: Context
) {
    AndroidView(
        factory = { ctx ->
            Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))

            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                setMultiTouchControls(true)
                controller.setZoom(15.0) // Zoom inicial
            }
        },
        update = { mapView ->
            // Esta parte se ejecuta cuando cambia la 'ubicacion'
            ubicacion?.let { ubi ->
                val puntoActual = GeoPoint(ubi.lat, ubi.ing)

                // Centrar el mapa
                mapView.controller.setCenter(puntoActual)

                // Limpiar marcadores antiguos para no acumularlos
                mapView.overlays.clear()

                // Marcador posición actual
                val marker = Marker(mapView)
                marker.position = puntoActual
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.title = "Yo"
                mapView.overlays.add(marker)

                // (Opcional) Dibujar también los puntos guardados
                puntosGuardados.forEach { guardado ->
                    val m = Marker(mapView)
                    m.position = GeoPoint(guardado.lat, guardado.ing)
                    m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    m.title = "Guardado"
                    // Puedes cambiar el icono aquí si quieres diferenciarlo
                    mapView.overlays.add(m)
                }

                mapView.invalidate() // Refrescar mapa
            }
        }
    )
}