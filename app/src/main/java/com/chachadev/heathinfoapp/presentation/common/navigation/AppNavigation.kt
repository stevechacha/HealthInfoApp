package com.chachadev.heathinfoapp.presentation.common.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.chachadev.heathinfoapp.presentation.dashboard.DashboardScreen

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
//        composable<Destination.App.SendMoney> {
//            SendMoneyMainScreen(
//                onNavigateToSendMoneyDetail = {},
//                onGoBack = onGoBack
//            )
//        }
//        composable<Destination.App.Loans> {
//            LoanScreen(
//                navigateToLoanMiniStatement = {},
//                onGoBack = onGoBack
//            )
//        }
//        composable<Destination.App.LoanStatement> {
//            LoanMiniStatementScreen()
//        }
//        composable<Destination.App.Deposit> {
//            DepositScreen(
//                onGoBack = onGoBack,
//                onclickContinue = {}
//            )
//        }
//        composable<Destination.App.Withdraw> {
//            WithdrawScreen(
//                onGoBack = onGoBack,
//                onClickContinue = {}
//            )
//        }
//        composable<Destination.App.BuyAirtime> {
//            BuyAirtimeScreen(
//                onGoBack = onGoBack,
//                onClickContinue = {}
//            )
//        }
//
//        composable<Destination.App.BankTransfer> {
//            BankTransferScreen(
//                onGoBack = onGoBack,
//                onClickContinue = {}
//            )
//        }

    }
}



