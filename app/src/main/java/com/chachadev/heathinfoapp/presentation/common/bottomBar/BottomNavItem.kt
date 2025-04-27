package com.chachadev.heathinfoapp.presentation.common.bottomBar

import com.chachadev.heathinfoapp.presentation.common.navigation.Destination
import com.chachadev.heathinfoapp.R


enum class BottomNavigation(
    val title: String,
    val icon: Int?,
    val contentDescription: String?,
    val route: Destination
) {
    PATIENT("Patients", R.drawable.home,"Patients", Destination.App.DashBoard.PatientList),
    PROGRAMS("Programs", R.drawable.receipt_long_24px,"Programs", Destination.App.DashBoard.Programs),
    FABACTION("",null,"", Destination.App.DashBoard.FabAction),
    ENROLL("Enroll", R.drawable.baseline_workspaces_filled_24,"Enroll", Destination.App.DashBoard.Enrollment),
    MORE("More", R.drawable.outline_more_horiz_24, "More", Destination.App.DashBoard.More),

}

