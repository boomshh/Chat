package com.example.chata.Navigation

sealed class Screen(val route: String) {

    // 페이지 간 경로 루트
    object LoginScreen: Screen("loginscreen")
    object SignupScreen: Screen("signupscreen")
    object ChatRoomsScreen:Screen("chatroomscreen")
    object ChatScreen:Screen("chatscreen")


}
