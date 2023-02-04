package fi.dy.ose.mobilecomputing.ui

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import fi.dy.ose.mobilecomputing.R

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier,
    prefs: SharedPreferences
){
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val context = LocalContext.current as FragmentActivity
    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Icon(
            painter = rememberVectorPainter(Icons.Filled.Person),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .size(60.dp),
            value = username.value,
            onValueChange = { text -> username.value = text},
            label = { Text(text = "Username") },
            shape = RoundedCornerShape(corner = CornerSize(50.dp)),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .size(60.dp),
            value = password.value,
            onValueChange = { passwordString -> password.value = passwordString },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(corner = CornerSize(50.dp)),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { checkCreds(username.value, password.value, sharedPreferences = prefs, navController = navController)},
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .size(55.dp),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { showBiometricPrompt(context, navController) },
            enabled = true,
            modifier = Modifier.fillMaxWidth().size(55.dp),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        ) {
            Text(text = "Use biometric authentication")
        }
    }
}

private fun checkCreds(username: String, password: String, sharedPreferences: SharedPreferences, navController: NavController) {
    var USERNAME_KEY = "user"
    var PASSWORD_KEY = "password"
    val correctUsername = sharedPreferences.getString(USERNAME_KEY, "").toString()
    val correctPassword = sharedPreferences.getString(PASSWORD_KEY, "").toString()

    if (username == correctUsername && password == correctPassword) {
        navController.navigate(route = "home")
    }

}

// https://fvilarino.medium.com/adding-a-pin-screen-with-biometric-authentication-in-jetpack-compose-a9bf7bd8acc9
private fun showBiometricPrompt(context: FragmentActivity, navController: NavController) {
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Use biometric authentication")
        .setNegativeButtonText("Cancel")
        .build()

    val biometricPrompt = BiometricPrompt(
        context,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                navController.navigate(route = "home")
            }

            override fun onAuthenticationFailed() {
                Toast.makeText(
                    context,
                    "Biometric authentication Failed",
                    Toast.LENGTH_LONG
                ).show()
            }
            }
    )
    biometricPrompt.authenticate(promptInfo)
}