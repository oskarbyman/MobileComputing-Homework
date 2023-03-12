package fi.dy.ose.mobilecomputing.ui.maps

import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import fi.dy.ose.mobilecomputing.ui.util.rememberMapViewWithLifeCycle
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun ReminderLocation(navController: NavController) {
    val mapView: MapView = rememberMapViewWithLifeCycle()
    val coroutineScope = rememberCoroutineScope()
    val locationState = remember { mutableStateOf<LatLng?>(null)}

    AndroidView({mapView}) {
        coroutineScope.launch {
            val map = mapView.awaitMap()
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isScrollGesturesEnabled = true
            val location = if (locationState.value != null) {
                locationState.value
            } else {
                LatLng(60.9929, 24.4590)}
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location!!.latitude, location!!.longitude),
                    15f
                )
            )
            setMapLongClick(map, navController, locationState)
        }
    }
    locationState.value?.let { location ->
        //navController.popBackStack()
        navController.previousBackStackEntry?.savedStateHandle?.set("location_data", location)
    }
}

private fun setMapLongClick(
    map: GoogleMap,
    navController: NavController,
    locationState: MutableState<LatLng?>
) {
    map.setOnMapLongClickListener { latlng ->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng: %2$.2f",
            latlng.latitude,
            latlng.longitude
        )
        map.addMarker(
            MarkerOptions().position(latlng).title("Reminder location").snippet(snippet)
        ).apply {
            locationState.value = latlng
        }
    }
}