package com.example.notes.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.notes.biometric.BiometricPromptManager


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun BiometricScreen(promptManager: BiometricPromptManager) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Show biometric prompt
        LaunchedEffect(Unit) {
            promptManager.showBiometricPrompt(
                "Biometric Authentication",
                "Enter with your fingerprint"
            )
        }
    }
}
