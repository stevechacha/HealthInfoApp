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
        @Serializable data object Avatar : Destination
        @Serializable data object UserName : Destination
        @Serializable data object Welcome: Destination
        @Serializable data object Login: Destination
        @Serializable data object PhoneRegister: Destination
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
            @Serializable data object Home : Destination
            @Serializable data object Transact : Destination
            @Serializable data object Loan : Destination
            @Serializable data object Discover : Destination
            @Serializable data object Account : Destination
        }

        @Serializable data class BuyGoodsDetailsRoute(val id: String): Destination
        @Serializable data object SendMoney: Destination
        @Serializable data object BuyAirtime: Destination
        @Serializable data object Loans: Destination
        @Serializable data object LoanStatement: Destination
        @Serializable data object Withdraw: Destination
        @Serializable data object Deposit: Destination
        @Serializable data object BankTransfer: Destination
        @Serializable data object Savings : Destination
        @Serializable data object WeSendMoney: Destination
        @Serializable data object WePayBill: Destination
        @Serializable data object BuyGoods: Destination
        @Serializable data object PayBillOption: Destination
        @Serializable data object PayBill: Destination
        @Serializable data object AboutsUs : Destination
        @Serializable data object Settings : Destination
        @Serializable data object NotificationSetting : Destination
        @Serializable data object NotificationHistory : Destination
        @Serializable data object ChangeLanguage : Destination
        @Serializable data object NotificationPreference : Destination
        @Serializable data object ResetPassword : Destination
        @Serializable data object ResetPin : Destination
        @Serializable data object ThemeSetting : Destination
        @Serializable data object PrivacyPolicy: Destination
        @Serializable data object TransactionAlert: Destination
        @Serializable data object ReminderNotification: Destination
        @Serializable data object SaccoAccountNotification: Destination
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