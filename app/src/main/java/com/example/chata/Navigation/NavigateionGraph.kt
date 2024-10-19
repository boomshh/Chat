package com.example.chata.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chata.AuthViewModel
import com.example.chata.screen.ChatRoomListScreen
import com.example.chata.screen.LoginScreen
import com.example.chata.screen.SignUpScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignupScreen.route
    ) {
        composable(Screen.SignupScreen.route) {
            SignUpScreen (
                authViewModel = authViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen.route)
                }
            )
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignupScreen.route)
                }
            ) {
                navController.navigate(Screen.ChatRoomsScreen.route)
            }
        }

        composable(Screen.ChatRoomsScreen.route) {
            ChatRoomListScreen()
        }
    }
}