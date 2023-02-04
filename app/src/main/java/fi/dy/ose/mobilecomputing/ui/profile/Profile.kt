package fi.dy.ose.mobilecomputing.ui.profile

import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fi.dy.ose.mobilecomputing.R

@Composable
fun Profile(
    sharedPreferences: SharedPreferences,
    onBackPress: () -> Unit
) {
    val openChangeUsernameAndPasswordDialog = remember { mutableStateOf(false) }
    val currentUsername = sharedPreferences.getString("user", "").toString()
    Surface() {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar() {
                IconButton(onClick = onBackPress) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
                Text(text = stringResource(R.string.profile))
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Person),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(150.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Username ${getUsername(sharedPreferences)}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { openChangeUsernameAndPasswordDialog.value = true }
                ) {
                    Text(text = stringResource(R.string.change_username))
                }
                if (openChangeUsernameAndPasswordDialog.value) {
                    val currentPassword = remember { mutableStateOf("") }
                    val newUsername = remember { mutableStateOf("") }
                    val newPassword = remember { mutableStateOf("") }
                    val confirmPassword = remember { mutableStateOf("") }
                    AlertDialog(
                        onDismissRequest = { openChangeUsernameAndPasswordDialog.value = false },
                        title = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(text = stringResource(R.string.change_username))

                            }
                        },
                        text = {
                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "To change your username and or password, set it and press save.")
                                Text(text = "You can change either one or both.")
                                Spacer(modifier = Modifier.height(10.dp))
                                Text( text = "Current username: $currentUsername")
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = currentPassword.value,
                                    onValueChange = { currentPasswordString -> currentPassword.value = currentPasswordString},
                                    label = { Text(text = "Current password")}
                                )
                                OutlinedTextField(
                                    value = newUsername.value,
                                    onValueChange = { usernameString -> newUsername.value = usernameString},
                                    label = { Text(text = "New Username")}
                                )
                                OutlinedTextField(
                                    value = newPassword.value,
                                    onValueChange = { passwordString -> newPassword.value = passwordString},
                                    label = { Text(text = "New Password")}
                                )
                                OutlinedTextField(
                                    value = confirmPassword.value,
                                    onValueChange = { passwordString -> confirmPassword.value = passwordString},
                                    label = { Text(text = "Confirm new Password")}
                                )
                            }
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    if (newPassword.value == confirmPassword.value){
                                        changeUsernameAndPassword(sharedPreferences,currentPassword = currentPassword.value, newUsername = newUsername.value, newPassword = newPassword.value)
                                        openChangeUsernameAndPasswordDialog.value = false
                                    }
                                }
                            ) {
                                Text(text = "Save")
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { openChangeUsernameAndPasswordDialog.value = false }
                            ) {
                                Text(text = "Cancel")
                            }
                        }
                    )
                }
            }

        }
    }
}

fun getUsername(prefs: SharedPreferences): String {
    return prefs.getString("user", "").toString()
}

fun changeUsernameAndPassword(prefs: SharedPreferences, currentPassword: String, newUsername: String, newPassword: String) {
    var USERNAME_KEY = "user"
    var PASSWORD_KEY = "password"
    val correctPassword = prefs.getString(PASSWORD_KEY, "").toString()

    if (currentPassword == correctPassword) {
        val editor = prefs.edit()
        if (newUsername.isNotEmpty()) editor.putString(USERNAME_KEY, newUsername)
        if (newPassword.isNotEmpty()) editor.putString(PASSWORD_KEY, newPassword)
        editor.apply()
    }
}