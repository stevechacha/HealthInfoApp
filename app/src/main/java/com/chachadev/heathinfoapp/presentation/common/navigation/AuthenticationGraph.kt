package com.chachadev.heathinfoapp.presentation.common.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.chachadev.heathinfoapp.presentation.auth.forgotPassword.ForgotPasswordScreen
import com.chachadev.heathinfoapp.presentation.auth.login.LoginScreen
import com.chachadev.heathinfoapp.presentation.auth.registration.RegistrationScreen
import com.chachadev.heathinfoapp.presentation.welcome.WelcomeScreen

fun NavGraphBuilder.authenticationGraph(
    onNavigate: (Destination) -> Unit,
    onNavigateGraph: (NavigationGraph, Boolean) -> Unit,
){
    navigation<NavigationGraph.Auth>(startDestination = Destination.Auth.Login){
        composable<Destination.Auth.Welcome> {
            WelcomeScreen(
                onNavigateToOnboarding = {}
            )
        }
      
        composable<Destination.Auth.Login>{
            LoginScreen(
                onNavigateToHomeDashBoard = { onNavigateGraph(NavigationGraph.App,true)},
                navigateBack = { }
            )
        }

        composable<Destination.Auth.RegisterDetails>{
            RegistrationScreen(
                onNavigateUp = { },
                onClickContinue = { onNavigate(Destination.Auth.RegistrationSubmit)}
            )
        }
     
        composable<Destination.Auth.ForgotPassword> {
            ForgotPasswordScreen(

            )
        }
    }
}

