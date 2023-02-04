package fi.dy.ose.mobilecomputing.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import fi.dy.ose.mobilecomputing.ui.theme.MobileComputingProjectTheme

class MainActivity : FragmentActivity() {
    lateinit var sharedPreferences: SharedPreferences
    var PREFS_KEY = "prefs"
    var USERNAME_KEY = "user"
    var PASSWORD_KEY = "password"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(USERNAME_KEY, "test")
            editor.putString(PASSWORD_KEY, "test")
            editor.apply()

            MobileComputingProjectTheme () {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MobileComputingApp(sharedPreferences)
                }
            }
        }
    }
}

