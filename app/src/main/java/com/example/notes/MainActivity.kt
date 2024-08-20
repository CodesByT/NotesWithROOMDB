package com.example.notes

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.*
import com.example.notes.biometric.BiometricPromptManager
import com.example.notes.presentation.BiometricScreen
import com.example.notes.presentation.Homescreen
import com.example.notes.presentation.SettingsScreen
import com.example.notes.presentation.sharedpreferences.PreferencesManager
import com.example.notes.ui.theme.NotesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var preferencesManager: PreferencesManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        preferencesManager = PreferencesManager(this)

        setContent {
            NotesTheme {
                val navController = rememberNavController()
                val promptManager = remember { BiometricPromptManager(this) }
                val scope = rememberCoroutineScope()
                var startDestination by remember { mutableStateOf("home_screen") }

                // Determine the start destination based on the biometric preference
                LaunchedEffect(Unit) {
                    if (preferencesManager.isBiometricEnabled()) {
                        startDestination = "biometric_screen"
                    } else {
                        startDestination = "home_screen"
                    }
                }

                // Collect biometric prompt results
                LaunchedEffect(Unit) {
                    scope.launch {
                        promptManager.promptResults.collect { result ->
                            if (result is BiometricPromptManager.BiometricResult.AuthenticationSuccess) {
                                navController.navigate("home_screen") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    }
                }

                // Navigation host with dynamic start destination
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("home_screen") {
                        Homescreen(navController)
                    }
                    composable("settings_screen") {
                        SettingsScreen(navController, preferencesManager)
                    }
                    composable("biometric_screen") {
                        BiometricScreen(promptManager = promptManager)
                    }
                }
            }
        }
    }
//    override fun onPause() {
//        super.onPause()
//
//        // Navigate to the biometric screen if biometric authentication is enabled
//        if (preferencesManager.isBiometricEnabled()) {
//            navController.navigate("biometric_screen") {
//                popUpTo(0) { inclusive = true }
//            }
//        }
//    }
}
