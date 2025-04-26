package com.chachadev.heathinfoapp.presentation.auth.splash

import androidx.compose.runtime.Composable

@Composable
fun SplashScreen(
    onNavigateToWelcome: () -> Unit,
    onNavigateToAuth: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    onNavigateToUnLock: () -> Unit,
    onNavigateToCorruptedSecurity: () -> Unit,
) {

    SplashScreenUI()
}
