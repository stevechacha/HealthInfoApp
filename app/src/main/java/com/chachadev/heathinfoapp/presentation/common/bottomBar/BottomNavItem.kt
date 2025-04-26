package com.chachadev.heathinfoapp.presentation.common.bottomBar

import androidx.compose.ui.graphics.vector.ImageVector
import com.chachadev.heathinfoapp.presentation.common.navigation.Destination
import com.chachadev.heathinfoapp.R


enum class BottomNavigation(
    val title: String,
    val icon: Int?,
    val contentDescription: String?,
    val route: Destination
) {
    HOME("Home", R.drawable.ic_launcher_background,"Home", Destination.App.DashBoard.Home),
    TRANSACT("Transact", R.drawable.ic_launcher_background,"Transact", Destination.App.DashBoard.Transact),
    LOAN("LOAN",null,"", Destination.App.DashBoard.Loan),
    DISCOVER("Discover", R.drawable.ic_launcher_background,"Discover", Destination.App.DashBoard.Discover),
    ACCOUNT("More", R.drawable.ic_launcher_background, "More", Destination.App.DashBoard.Account),

}

