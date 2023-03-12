package fi.dy.ose.mobilecomputing.ui.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import fi.dy.ose.mobilecomputing.R
import fi.dy.ose.mobilecomputing.ui.Graph
import fi.dy.ose.mobilecomputing.ui.home.reminderCard.ReminderCardList
import java.util.*

@Composable
fun Home(
    viewModel: HomeViewModel = viewModel(),
    navController: NavController
) {
    val viewState by viewModel.state.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        HomeContent(
            navController = navController
        )
    }
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeContent(
    navController: NavController,
) {
    val latitudeHome = remember { mutableStateOf<Float?>(null)  }
    val longitudeHome = remember { mutableStateOf<Float?>(null)  }
    val locationData = navController.currentBackStackEntry?.savedStateHandle?.get<LatLng>("location_data")
    if (locationData != null) {
        latitudeHome.value = locationData.latitude.toFloat()
        longitudeHome.value = locationData.longitude.toFloat()
        navController.currentBackStackEntry?.savedStateHandle?.set("location_data", null)
    }
    Scaffold(
        modifier = Modifier.padding(bottom = 24.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(route = "reminder")},
                contentColor = Color.Black,
                backgroundColor = Color.Green,
                modifier = Modifier.padding(all = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null)
            }
        }
    ) {
        Column (
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
        ) {
            val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)

            HomeAppBar(
                backgroundColor = appBarColor,
                latitude = latitudeHome,
                longitude = longitudeHome,
                navController = navController
            )
            ReminderCardList(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                latitude = latitudeHome.value,
                longitude = longitudeHome.value
            )
            
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeAppBar(
    backgroundColor: Color,
    latitude: MutableState<Float?>,
    longitude: MutableState<Float?>,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .heightIn(max = 24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
            Text(text = if (latitude.value != null && longitude.value != null) {
                String.format(
                    Locale.getDefault(),
                    "Lat: %1$.2f, Lng: %2$.2f",
                    latitude.value,
                    longitude.value
                )
            } else {
                "Set virtual location"
            }, modifier = Modifier.combinedClickable(
                onClick = {},
                onLongClick = {
                    latitude.value = null
                    longitude.value = null
                    Toast.makeText(Graph.appContext, "Cleared virtual location", Toast.LENGTH_SHORT).show()
                }
            )

            )
            IconButton(
                onClick = { navController.navigate(route = "map") }
            ) {
                Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "Set virtual location")

            }
            IconButton(onClick = { navController.navigate(route = "profile") }) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = stringResource(R.string.account))

            }
        }
    )
}
