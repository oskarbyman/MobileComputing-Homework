package fi.dy.ose.mobilecomputing.ui.reminder

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import fi.dy.ose.code.domain.entity.Reminder
import fi.dy.ose.mobilecomputing.ui.Graph
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ReminderScreen(
    navController: NavController,
    sharedPrefs: SharedPreferences,
    viewModel: ReminderViewModel = hiltViewModel(),
    reminder_id: Long? = null,
) {
    Surface() {
        val title = remember { mutableStateOf("") }
        val note = remember { mutableStateOf("") }
        val date = remember { mutableStateOf(LocalDate.now()) }
        val time = remember { mutableStateOf(LocalTime.now()) }
        val latitude = remember { mutableStateOf<Float?>(null)  }
        val longitude = remember { mutableStateOf<Float?>(null)  }
        val useTimeReminder = remember { mutableStateOf(true) }

        val locationData = navController.currentBackStackEntry?.savedStateHandle?.get<LatLng>("location_data")
        if (locationData != null) {
            latitude.value = locationData.latitude.toFloat()
            longitude.value = locationData.longitude.toFloat()
            navController.currentBackStackEntry?.savedStateHandle?.set("location_data", null)
        }

        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {}
        )

        if (reminder_id != null) {
            val reminder: Reminder? = viewModel.loadReminder(reminder_id)

            if (reminder != null) {
                title.value = reminder.title
                note.value = reminder.message
                date.value = reminder.reminder_time.toLocalDate()
                time.value = reminder.reminder_time.toLocalTime()
            }
        }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar() {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription =  null
                    )
                }
                Text(text = "Reminder")
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = title.value,
                    onValueChange = { titleString -> title.value = titleString },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "Title") },
                    shape = RoundedCornerShape(corner = CornerSize(50.dp))
                )
                OutlinedTextField(
                    value = note.value,
                    onValueChange = { noteString -> note.value = noteString },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "Note") },
                    shape = RoundedCornerShape(corner = CornerSize(50.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row() {
                    showDatePicker(context = LocalContext.current as FragmentActivity, date = date)
                    Spacer(modifier = Modifier.height(16.dp))
                    showTimePicker(context = LocalContext.current as FragmentActivity, time = time)
                    Spacer(modifier = Modifier.height(16.dp))

                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = useTimeReminder.value,
                        onCheckedChange = { checked -> useTimeReminder.value = checked },
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(text = "Use time for reminder")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row() {
                    OutlinedButton(
                        onClick = {
                            requestPermission(
                                context = context,
                                permission = Manifest.permission.ACCESS_FINE_LOCATION,
                                requestPermission = {
                                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                                }
                            )
                            requestPermission(
                                context = context,
                                permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                requestPermission = {
                                    launcher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                                }
                            ).apply {
                                navController.navigate("map")
                            }
                        },
                        modifier = if (latitude.value != null && longitude.value != null) {
                            Modifier
                                .fillMaxWidth(0.5f)
                                .size(55.dp)
                        } else {
                            Modifier
                                .fillMaxWidth()
                                .size(55.dp)
                        }
                    ) {
                        Text(
                            text = if (latitude.value != null && longitude.value != null) {
                                String.format(
                                    Locale.getDefault(),
                                    "Lat: %1$.2f, Lng: %2$.2f",
                                    latitude.value,
                                    longitude.value
                                )
                            } else {
                                "Set reminder location"
                            }
                        )
                    }
                    if (latitude.value != null && longitude.value != null) {
                        OutlinedButton(
                            onClick = {
                                latitude.value = null
                                longitude.value = null
                                Toast.makeText(Graph.appContext, "Cleared location", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth(1f).height(55.dp)
                        ) {
                            Text(text = "Clear location")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                          viewModel.saveReminder(
                              navController = navController,
                              reminder_id = reminder_id,
                              useTime = useTimeReminder.value,
                              reminder = Reminder(
                                  title = title.value,
                                  message = note.value,
                                  reminder_time = date.value.atTime(time.value),
                                  creator_id = sharedPrefs.getString("user", "").toString(),
                                  location_x = latitude.value,
                                  location_y = longitude.value
                              )
                          )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(55.dp)
                ) {
                    Text(text = "Set reminder")
                }
            }
        }
    }
}

@Composable
fun showDatePicker(context: Context, date: MutableState<LocalDate>) {
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = LocalDate.of(year, month, dayOfMonth)
        }, year, month, day
    )

    OutlinedTextField(
        modifier = Modifier.clickable { datePickerDialog.show() },
        value = date.value.toString(),
        onValueChange = {},
        label = { Text(text = "Date") },
        shape = RoundedCornerShape(corner = CornerSize(50.dp)),
        enabled = false
    )
}

@Composable
fun showTimePicker( context: Context, time: MutableState<LocalTime> ) {
    val hour: Int = time.value.hour
    val minute: Int = time.value.minute

    val dialog = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            time.value = LocalTime.of(hour, minute)
        }, hour, minute, true
    )

    OutlinedTextField(
        modifier = Modifier.clickable { dialog.show() },
        label = { Text(text = "Time") },
        value = time.value.format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
        onValueChange = {},
        enabled = false,
        shape = RoundedCornerShape(corner = CornerSize(50.dp))
    )
}

private fun requestPermission(
    context: Context,
    permission: String,
    requestPermission: () -> Unit
) {
    if (ContextCompat.checkSelfPermission(
            context,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        requestPermission()
    }
}

