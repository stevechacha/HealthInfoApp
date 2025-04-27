package com.chachadev.heathinfoapp.presentation.common.navigation

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable


sealed interface NavigationGraph {
    @Serializable data object Loading : NavigationGraph
    @Serializable data object Auth : NavigationGraph
    @Serializable data object Security : NavigationGraph
    @Serializable data object App : NavigationGraph
    @Serializable data object Welcome : NavigationGraph

}


sealed interface Destination {
    @Serializable data object Loading : Destination
    @Serializable data object Welcome : Destination


    @Serializable
    data object Auth : Destination {
        @Serializable data object Welcome: Destination
        @Serializable data object Login: Destination
        @Serializable data object RegisterDetails: Destination
        @Serializable data object RegistrationSubmit: Destination
        @Serializable data object ForgotPassword: Destination
    }

    @Serializable data object Security : Destination {
        @Serializable data object Security : Destination
        @Serializable data object SecurityConfirmed : Destination
        @Serializable data object PIN : Destination
        @Serializable data object UnLock : Destination
        @Serializable data object CorruptedSecurity : Destination
    }

    @Serializable
    data object App : Destination {
        @Serializable data object DashBoard : Destination {
            @Serializable data object PatientList : Destination
            @Serializable data object Programs : Destination
            @Serializable data object FabAction : Destination
            @Serializable data object Enrollment : Destination
            @Serializable data object More : Destination
            @Serializable data object RegisterPatient : Destination
            @Serializable data object CreateProgram : Destination

        }

        @Serializable data class PatientDetailsRoute(val id: String): Destination
        @Serializable data object PatientEnrollment : Destination

    }

}



fun NavHostController.onNavigate(destination: Destination, finish: Boolean = false) {
    navigate(destination) {
        if (finish) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}

fun NavHostController.onNavigateBack(destination: Destination) {
    popBackStack(destination, inclusive = false)
}

fun NavHostController.onNavigateGraph(graph: NavigationGraph, finish: Boolean = false) {
    navigate(graph) {
        if (finish) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}

fun NavHostController.onGoBack() {
    popBackStack()
}