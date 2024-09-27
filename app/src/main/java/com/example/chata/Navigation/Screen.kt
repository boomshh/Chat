package com.example.chata.Navigation

sealed class Screen(val route: String) {

    object LoginScreen: Screen("loginscreen")
    object SignupScreen: Screen("signupscreen")
    object ChatRoomsScreen:Screen("chatroomscreen")
    object ChatScreen:Screen("chatscreen")
}
