package com.chachadev.heathinfoapp.presentation.common.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.chachadev.heathinfoapp.presentation.dashboard.DashboardScreen
import com.chachadev.heathinfoapp.presentation.enrollPatient.EnrollPatientScreen
import com.chachadev.heathinfoapp.presentation.patientDetailsProfile.PatientDetailsScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.appNavigation(
    onNavigate: (Destination, Boolean) -> Unit,
    onNavigateBack: (Destination) -> Unit,
    onGoBack: () -> Unit,
) {
    navigation<NavigationGraph.App>(startDestination = Destination.App.DashBoard) {
        composable<Destination.App.DashBoard> {
            DashboardScreen(
                onNavigate = onNavigate
            )
        }
        composable<Destination.App.PatientDetailsRoute> { backStackEntry ->
            val patientId = backStackEntry.toRoute<Destination.App.PatientDetailsRoute>()
            PatientDetailsScreen(
                patientId = patientId.id,
            )
        }
        composable<Destination.App.PatientEnrollment> {
            EnrollPatientScreen(
                onBackClick =  {},
                onEnrollmentSuccess =  {}
            )
        }



    }
}



