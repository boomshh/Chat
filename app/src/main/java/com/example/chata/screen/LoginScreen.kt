package com.example.chata.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chata.viewmodel.AuthViewModel
import com.example.chata.data.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignUp : () -> Unit,
    onSignInSucess: () -> Unit

) {
    var email by remember { mutableStateOf("") }
    var password by remember {
        mutableStateOf("")
    }
    val result by authViewModel.authResult.observeAsState()
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = email,
            onValueChange = { email = it },
            label = { Text("Email")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp))

        OutlinedTextField(value = password,
            onValueChange = { password = it},
            label = { Text(text = "Password")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation())

        if(errorMessage.isNotEmpty()) {
            Text(text = errorMessage,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.padding(8.dp))
        }

        Button(
            onClick = {
                      authViewModel.login(email, password)
                when(result) {
                    is Result.Success -> {
                        onSignInSucess()
                    }
                    is Result.Error -> {
                        errorMessage = "아이디 또는 비밀번호를 확인해주세요."
                    }
                    else -> {
                    }
                }


            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Don't have an account? Sign up",
            modifier = Modifier.clickable { onNavigateToSignUp() })

        }



}