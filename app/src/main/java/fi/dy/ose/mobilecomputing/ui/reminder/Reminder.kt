package fi.dy.ose.mobilecomputing.ui.reminder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Reminder(
    onBackPress: () -> Unit
) {
    Surface() {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar() {
                IconButton(
                    onClick = onBackPress
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
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(50.dp),
                    label = { Text(text = "Note") },
                    shape = RoundedCornerShape(corner = CornerSize(50.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row() {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text(text = "Date") },
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text(text = "Time") },
                        shape = RoundedCornerShape(corner = CornerSize(50.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /*TODO*/ },
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