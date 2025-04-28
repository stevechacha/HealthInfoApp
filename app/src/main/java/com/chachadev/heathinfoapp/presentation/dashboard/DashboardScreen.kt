package com.chachadev.heathinfoapp.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.chachadev.heathinfoapp.presentation.common.bottomBar.StandardScaffold
import com.chachadev.heathinfoapp.presentation.common.composables.remember.rememberSnackBarState
import com.chachadev.heathinfoapp.presentation.common.navigation.Destination
import com.chachadev.heathinfoapp.presentation.createProgram.CreateProgramScreen
import com.chachadev.heathinfoapp.presentation.enrollPatient.EnrollPatientScreen
import com.chachadev.heathinfoapp.presentation.moreScreen.MoreScreen
import com.chachadev.heathinfoapp.presentation.patient.PatientRegistrationScreen
import com.chachadev.heathinfoapp.presentation.patientList.PatientsListScreen
import com.chachadev.heathinfoapp.presentation.programsList.ProgramsListScreen
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    onNavigate: (Destination, Boolean) -> Unit,
) {
    val snackBarHostState = rememberSnackBarState()
    val scope = rememberCoroutineScope()
    var fabExpanded by remember { mutableStateOf(false) }
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

    StandardScaffold(
        navController = navigator,
        modifier = Modifier.fillMaxSize(),
        onFabClick = { fabExpanded = !fabExpanded },
        isLoggedIn = true,
        snackBarHostState = snackBarHostState,
    ) {
        NavHost(navigator, startDestination = Destination.App.DashBoard.PatientList) {
            composable<Destination.App.DashBoard.PatientList> {
                PatientsListScreen(
                    onPatientClick = { patientId ->
                        onNavigate(
                            Destination.App.PatientDetailsRoute(patientId),
                            false
                        )
                    }
                )
            }
            composable<Destination.App.DashBoard.Programs> {
                ProgramsListScreen(
                    onCreateNewClick = {
                        onNavigate(Destination.App.DashBoard.CreateProgram, false)
                    },
                    onProgramClick = {}
                )
            }
            composable<Destination.App.DashBoard.Enrollment> {
                EnrollPatientScreen(
                    onBackClick = { navigator.popBackStack() },
                    onEnrollmentSuccess = {}
                )
            }
            composable<Destination.App.DashBoard.More> {
                MoreScreen(
                    onBackClick = { navigator.popBackStack() }
                )
            }
            composable<Destination.App.DashBoard.RegisterPatient> {
                PatientRegistrationScreen()
            }

            composable<Destination.App.DashBoard.CreateProgram> {
                CreateProgramScreen(
                    onBackClick = {},
                    onProgramCreated = {}
                )
            }
        }
    }
}


