package com.chachadev.heathinfoapp.presentation.common.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.chachadev.heathinfoapp.presentation.auth.splash.SplashScreen

fun NavGraphBuilder.splashNavigation(
    onNavigate: (Destination, Boolean) -> Unit,
    onNavigateGraph: (NavigationGraph, Boolean) -> Unit,
) {
    navigation<NavigationGraph.Loading>(startDestination = Destination.Loading) {
        composable<Destination.Loading> {
            SplashScreen(
                onNavigateToWelcome = {
                    onNavigateGraph(NavigationGraph.Welcome, true)
                },
                onNavigateToAuth = {
                    onNavigateGraph(NavigationGraph.Auth, true)
                },
                onNavigateToSecurity = {
                    onNavigateGraph(NavigationGraph.Security, true)
                },
                onNavigateToDashboard = {
                    onNavigateGraph(NavigationGraph.App, true)
                },
                onNavigateToCorruptedSecurity = {
                    onNavigate(Destination.Security.CorruptedSecurity, true)
                },
                onNavigateToUnLock = {
                    onNavigate(Destination.Security.UnLock, true)
                },
            )
        }
    }
}