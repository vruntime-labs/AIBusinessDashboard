package com.aibusiness.dashboard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aibusiness.dashboard.ui.screens.*
import com.aibusiness.dashboard.viewmodel.MainViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Generate : Screen("generate")
    object History : Screen("history")
    object Settings : Screen("settings")
    object Result : Screen("result")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (uiState.isLoggedIn) Screen.Dashboard.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    viewModel.login()
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                uiState = uiState,
                onNavigateToGenerate = { navController.navigate(Screen.Generate.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Generate.route) {
            GenerateScreen(
                uiState = uiState,
                onPromptChange = viewModel::updatePrompt,
                onFormatChange = viewModel::updateFormat,
                onStyleChange = viewModel::updateStyle,
                onThemeChange = viewModel::updateTheme,
                onTypeChange = viewModel::updateType,
                onAddFiles = viewModel::addFiles,
                onRemoveFile = viewModel::removeFile,
                onClearFiles = viewModel::clearFiles,
                onUseRealAIChange = viewModel::setUseRealAI,
                onGenerate = {
                    viewModel.generate()
                    // Navigate to result after a short delay or when result arrives
                },
                onBack = { navController.popBackStack() },
                onViewResult = { navController.navigate(Screen.Result.route) }
            )
        }

        composable(Screen.Result.route) {
            ResultScreen(
                result = uiState.latestResult,
                isLoading = uiState.isLoading,
                onBack = { navController.popBackStack() },
                onNewGeneration = {
                    viewModel.clearLatestResult()
                    navController.navigate(Screen.Generate.route) {
                        popUpTo(Screen.Dashboard.route)
                    }
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                history = uiState.history,
                onBack = { navController.popBackStack() },
                onDeleteItem = viewModel::deleteHistoryItem,
                onClearAll = viewModel::clearHistory,
                onItemClick = { /* Can navigate to detail if needed */ }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                uiState = uiState,
                onApiKeyChange = viewModel::setApiKey,
                onUseRealAIChange = viewModel::setUseRealAI,
                onToggleDarkTheme = viewModel::toggleDarkTheme,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
