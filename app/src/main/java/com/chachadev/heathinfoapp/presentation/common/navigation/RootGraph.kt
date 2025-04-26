package com.chachadev.heathinfoapp.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.koin.androidx.compose.koinViewModel


@Composable
fun NavigationGraph(navHostController: NavHostController){
    NavHost(
        navController = navHostController,
        startDestination = NavigationGraph.App
    ){
        welcomeNavigation(
            onNavigateGraph = navHostController::onNavigateGraph
        )
        splashNavigation(
            onNavigateGraph = navHostController::onNavigateGraph,
            onNavigate = navHostController::onNavigate,
        )
        authenticationGraph(
            onNavigate = navHostController::onNavigate,
            onNavigateGraph = navHostController::onNavigateGraph,
        )
        appNavigation(
            onNavigate = navHostController::onNavigate,
            onNavigateBack = navHostController::onNavigateBack,
            onGoBack = navHostController::onGoBack,
        )
    }
}


@Composable
private inline fun <reified T: ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return koinViewModel<T>()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(
        viewModelStoreOwner = parentEntry
    )
}



