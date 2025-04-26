package com.chachadev.heathinfoapp.presentation.common.bottomBar

import com.chachadev.heathinfoapp.presentation.common.navigation.Destination
import com.chachadev.heathinfoapp.R


enum class BottomNavigation(
    val title: String,
    val icon: Int?,
    val contentDescription: String?,
    val route: Destination
) {
    HOME("Home", R.drawable.ic_launcher_background,"Home", Destination.App.DashBoard.Patients),
    TRANSACT("Transact", R.drawable.ic_launcher_background,"Transact", Destination.App.DashBoard.Programs),
    LOAN("LOAN",null,"", Destination.App.DashBoard.FabAction),
    DISCOVER("Discover", R.drawable.ic_launcher_background,"Discover", Destination.App.DashBoard.Enrollment),
    ACCOUNT("More", R.drawable.ic_launcher_background, "More", Destination.App.DashBoard.Profile),

}

