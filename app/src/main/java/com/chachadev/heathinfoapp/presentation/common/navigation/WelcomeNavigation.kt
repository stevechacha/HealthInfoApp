package com.chachadev.heathinfoapp.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.chachadev.heathinfoapp.presentation.welcome.WelcomeScreen

fun NavGraphBuilder.welcomeNavigation(
    onNavigateGraph: (NavigationGraph, Boolean) -> Unit,
) {
    navigation<NavigationGraph.Welcome>(startDestination = Destination.Welcome) {
        composable<Destination.Welcome> {
            WelcomeScreen(
                onNavigateToOnboarding = { onNavigateGraph(NavigationGraph.Auth, true) }
            )
        }
    }
}

