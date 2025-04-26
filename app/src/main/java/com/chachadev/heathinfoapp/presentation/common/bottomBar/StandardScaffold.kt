package com.chachadev.heathinfoapp.presentation.common.bottomBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.chachadev.heathinfoapp.R


@Composable
fun StandardScaffold(
    navController: NavController,
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    isLoggedIn: Boolean,
    onFabClick: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination


    val showBottomBar = BottomNavigation.entries.map { it.route::class }.any { route ->
        currentDestination?.hierarchy?.any { it.hasRoute(route) } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    BottomNavigation.entries.forEachIndexed { i, bottomNavItem ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.hasRoute(bottomNavItem.route::class) } == true
                        StandardBottomNavItem(
                            icon = bottomNavItem.icon,
                            title = bottomNavItem.title,
                            contentDescription = bottomNavItem.contentDescription,
                            selected = isSelected,
                            enabled = bottomNavItem.icon != null
                        ) {
                            if (currentDestination !=null) {
                                navController.navigate(bottomNavItem.route) {
                                    launchSingleTop = false
                                    popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                }
                            }

                        }
                    }
                }
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                FloatingActionButton(
                    modifier = Modifier.offset(y = (72).dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    onClick = onFabClick,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        focusedElevation = 0.dp,
                        hoveredElevation = 0.dp
                    ),
                    shape = CircleShape
                ) {
                    Box(
                        modifier = Modifier.padding(8.dp).size(54.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_background),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier
    ) {
        content(it)
//        SnackBarUI(
//            paddingTop = it.calculateTopPadding(),
//            data = snackBarHostState.currentSnackbarData,
//        )
    }
}




