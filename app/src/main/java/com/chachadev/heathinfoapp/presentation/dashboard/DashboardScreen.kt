package com.chachadev.heathinfoapp.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.chachadev.heathinfoapp.presentation.clientProgram.CreateProgramScreen
import com.chachadev.heathinfoapp.presentation.common.bottomBar.StandardScaffold
import com.chachadev.heathinfoapp.presentation.common.composables.remember.rememberSnackBarState
import com.chachadev.heathinfoapp.presentation.common.navigation.Destination
import com.chachadev.heathinfoapp.presentation.enrollClient.EnrollPatientScreen
import com.chachadev.heathinfoapp.presentation.patient.PatientScreen
import com.chachadev.heathinfoapp.presentation.patientList.PatientsListScreen
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    onNavigate: (Destination, Boolean) -> Unit,
) {
    val snackBarHostState = rememberSnackBarState()
    val scope = rememberCoroutineScope()
    var addButtonIsExpanded by remember { mutableStateOf(false) }
    val navigator = rememberNavController()
    val navBackStackEntry by navigator.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination



    fun onError(error: String) {
        scope.launch {
            snackBarHostState.showSnackbar(error)
        }
    }

    fun isCurrentRoute(route: String?): Boolean =
        currentDestination?.hierarchy?.any { it.route == route } == true

    fun onNavigate(destination: Destination) {
        if (!isCurrentRoute(destination::class.qualifiedName))
            navigator.navigate(destination)

        addButtonIsExpanded = false
    }

    StandardScaffold(
        navController = navigator,
        modifier = Modifier.fillMaxSize(),
        onFabClick = {  },
        isLoggedIn = true,
        snackBarHostState = snackBarHostState
    ) {
        NavHost(navigator, startDestination = Destination.App.DashBoard.Patients) {
            composable<Destination.App.DashBoard.Patients> {
                PatientsListScreen(
                    onPatientClick = { patientId->
                        onNavigate(
                            Destination.App.PatientDetailsRoute(patientId),
                            false
                        )
                    }
                )
            }
            composable<Destination.App.DashBoard.Programs> {
                ProgramsListScreen()
            }

            composable<Destination.App.DashBoard.Enrollment> {
                EnrollPatientScreen(
                    onBackClick = {},
                    onEnrollmentSuccess = {}
                )
            }

            composable<Destination.App.DashBoard.Profile> {
                EnrollPatientScreen(
                    onBackClick = {},
                    onEnrollmentSuccess = {}
                )
            }
        }
    }
}


@Composable
fun AccountScreen(onNavigate: (Destination, Boolean) -> Unit, onGoBack: () -> Unit) {
}
