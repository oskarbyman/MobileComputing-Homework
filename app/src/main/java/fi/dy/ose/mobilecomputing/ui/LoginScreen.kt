package fi.dy.ose.mobilecomputing.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import fi.dy.ose.mobilecomputing.R

@Composable
fun LoginScreen(
    modifier: Modifier
){
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

       /* Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "login_image"
        )*/

        Icon(
            painter = rememberVectorPainter(Icons.Filled.Person),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth().size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().size(50.dp),
            value = username.value,
            onValueChange = { text -> username.value = text},
            label = { Text(text = "Username") },
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().size(50.dp),
            value = password.value,
            onValueChange = { passwordString -> password.value = passwordString },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth().size(55.dp),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        ) {
            Text(text = "Login")
        }

    }
}