package com.anima.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.anima.service.AuthService
import com.anima.service.AuthState

@Composable
fun AuthScreen(
    authService: AuthService,
    onAuthSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var isSignUp by remember { mutableStateOf(false) }

    val authState by authService.authState.collectAsState()
    val authError by authService.authError.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onAuthSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isSignUp) "Регистрация" else "Вход",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        if (isSignUp) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Имя") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Возраст") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("Страна") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        if (authError != null) {
            Text(
                text = authError!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = {
                if (isSignUp) {
                    // Регистрация
                    val ageInt = age.toIntOrNull() ?: 0
                    if (ageInt > 0) {
                        authService.signUp(name, ageInt, country)
                    }
                } else {
                    // Вход
                    authService.signIn(email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = if (isSignUp) "Зарегистрироваться" else "Войти",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        TextButton(
            onClick = { isSignUp = !isSignUp },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = if (isSignUp) "Уже есть аккаунт? Войти" else "Нет аккаунта? Зарегистрироваться",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (!isSignUp) {
            TextButton(
                onClick = { authService.resetPassword(email) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Забыли пароль?",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (authState is AuthState.Authenticating) {
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
} 