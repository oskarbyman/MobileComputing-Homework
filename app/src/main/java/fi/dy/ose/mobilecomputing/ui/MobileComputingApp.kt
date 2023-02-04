package fi.dy.ose.mobilecomputing.ui

import android.content.SharedPreferences
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fi.dy.ose.mobilecomputing.MobileComputingAppState
import fi.dy.ose.mobilecomputing.rememberMobileComputingAppState
import fi.dy.ose.mobilecomputing.ui.home.Home
import fi.dy.ose.mobilecomputing.ui.profile.Profile
import fi.dy.ose.mobilecomputing.ui.reminder.Reminder

@Composable
fun MobileComputingApp(
    sharedPreferences: SharedPreferences,
    appState: MobileComputingAppState = rememberMobileComputingAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(navController = appState.navController, modifier = Modifier.fillMaxSize(), prefs = sharedPreferences)
        }
        composable(route = "home") {
            Home(navController = appState.navController)
        }
        composable(route = "reminder") {
            Reminder(onBackPress = appState::navigateBack)
        }
        composable(route = "profile") {
            Profile(sharedPreferences, appState::navigateBack)
        }
    }
}