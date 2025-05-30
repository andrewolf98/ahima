package com.anima.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.service.AuthService
import com.anima.service.AuthState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    authService: AuthService,
    onComplete: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    var currentPage by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> OnboardingPage(
                    title = "Добро пожаловать в ANIMA",
                    description = "Ваш персональный помощник в борьбе с тревожностью и депрессией",
                    imageRes = "onboarding_1"
                )
                1 -> OnboardingPage(
                    title = "Ежедневные практики",
                    description = "Медитации, дыхательные упражнения и когнитивные техники для улучшения вашего состояния",
                    imageRes = "onboarding_2"
                )
                2 -> OnboardingPage(
                    title = "Отслеживайте прогресс",
                    description = "Анализируйте свое состояние и получайте персонализированные рекомендации",
                    imageRes = "onboarding_3"
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { page ->
                val color = if (page == currentPage) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = MaterialTheme.shapes.small,
                        color = color
                    ) {}
                }
            }
        }

        Button(
            onClick = {
                if (currentPage < 2) {
                    currentPage++
                } else {
                    onComplete()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = if (currentPage < 2) "Далее" else "Начать",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (currentPage > 0) {
            TextButton(
                onClick = { currentPage-- },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Назад",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    title: String,
    description: String,
    imageRes: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // TODO: Добавить изображение
        // Image(
        //     painter = painterResource(id = imageRes),
        //     contentDescription = null,
        //     modifier = Modifier
        //         .size(200.dp)
        //         .padding(bottom = 32.dp)
        // )

        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
} 