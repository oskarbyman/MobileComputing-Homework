package fi.dy.ose.mobilecomputing.ui

import android.content.SharedPreferences
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import fi.dy.ose.mobilecomputing.MobileComputingAppState
import fi.dy.ose.mobilecomputing.rememberMobileComputingAppState
import fi.dy.ose.mobilecomputing.ui.home.Home
import fi.dy.ose.mobilecomputing.ui.maps.ReminderLocation
import fi.dy.ose.mobilecomputing.ui.profile.Profile
import fi.dy.ose.mobilecomputing.ui.reminder.ReminderScreen
import fi.dy.ose.mobilecomputing.ui.reminder.ReminderViewModel

@Composable
fun MobileComputingApp(
    sharedPreferences: SharedPreferences,
    appState: MobileComputingAppState = rememberMobileComputingAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = "home"
    ) {
        composable(route = "login") {
            LoginScreen(navController = appState.navController, modifier = Modifier.fillMaxSize(), prefs = sharedPreferences)
        }
        composable(route = "home") {
            Home(navController = appState.navController)
        }
        composable(
            route = "reminder?reminderId={reminderId}",
            arguments = listOf(navArgument("reminderId") {
                defaultValue = null
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            ReminderScreen(navController = appState.navController, sharedPrefs = sharedPreferences, reminder_id = backStackEntry.arguments?.getString("reminderId")?.toLong())
        }
        composable(route = "profile") {
            Profile(sharedPreferences, appState::navigateBack)
        }
        composable(route = "map") {
            ReminderLocation(navController = appState.navController)
        }
    }
}