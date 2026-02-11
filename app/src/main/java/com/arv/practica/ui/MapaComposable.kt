package com.arv.practica.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
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
){
    AndroidView(
        factory = {ctx->
            Configuration.getInstance().load(ctx,ctx.getSharedPreferences("osmdroid",
                Context.MODE_PRIVATE))

            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)

                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                setMultiTouchControls(true)
                isClickable=true

               // controller.setCenter(GeoPoint(lat,ing))

                val marker = Marker(this)
               // marker.position= GeoPoint(lat,ing)

               // marker.position= GeoPoint(lat,ing)
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                overlays.add(marker)
            }

        }
    )
}