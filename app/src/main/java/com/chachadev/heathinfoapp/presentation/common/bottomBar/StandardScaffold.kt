package com.chachadev.heathinfoapp.presentation.common.bottomBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.chachadev.heathinfoapp.R
import com.chachadev.heathinfoapp.presentation.common.navigation.Destination

@OptIn(ExperimentalMaterial3Api::class)
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
    var fabExpanded by remember { mutableStateOf(false) }

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
                            if (currentDestination != null) {
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
                // Combined FAB and Menu
                Box {
                    // FAB Menu - shown when expanded
                    if (fabExpanded) {
                        FABMenu(
                            onDismiss = { fabExpanded = false },
                            onCreateProgram = {
                                navController.navigate(Destination.App.DashBoard.CreateProgram)
                                fabExpanded = false
                            },
                            onRegisterPatient = {
                                navController.navigate(Destination.App.DashBoard.RegisterPatient)
                                fabExpanded = false
                            },
                            onEnrollPatient = {
                                navController.navigate(Destination.App.DashBoard.Enrollment)
                                fabExpanded = false
                            }
                        )
                    }

                    // Main FAB Button
                    FloatingActionButton(
                        modifier = Modifier.offset(y = 72.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        onClick = { fabExpanded = !fabExpanded },
                        elevation = FloatingActionButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            focusedElevation = 0.dp,
                            hoveredElevation = 0.dp
                        ),
                        shape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(54.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (fabExpanded) Icons.Default.Close else Icons.Default.Add,
                                contentDescription = if (fabExpanded) "Close menu" else "Open menu",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = modifier
    ) {
        content(it)
    }
}

@Composable
fun FABMenu(
    onDismiss: () -> Unit,
    onCreateProgram: () -> Unit,
    onRegisterPatient: () -> Unit,
    onEnrollPatient: () -> Unit
) {
    Column(
        modifier = Modifier
            .widthIn(min = 200.dp)
            .padding(bottom = 80.dp) // Space for the FAB
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FABMenuItem(
            icon = Icons.Default.ListAlt,
            label = "Create Program",
            onClick = onCreateProgram
        )
        FABMenuItem(
            icon = Icons.Default.PersonAdd,
            label = "Register Patient",
            onClick = onRegisterPatient
        )
        FABMenuItem(
            icon = Icons.Default.AssignmentTurnedIn,
            label = "Enroll Patient",
            onClick = onEnrollPatient
        )
    }
}

@Composable
fun FABMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}



