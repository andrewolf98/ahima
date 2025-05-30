package com.anima.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anima.viewmodel.PaymentViewModel
import com.anima.data.payment.*
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(viewModel: PaymentViewModel = viewModel()) {
    val текущаяЦена by viewModel.текущаяЦена.collectAsState()
    val активныеБонусы by viewModel.активныеБонусы.collectAsState()
    val статусПокупки by viewModel.статусПокупки.collectAsState()
    val покупкаВПроцессе by viewModel.покупкаВПроцессе.collectAsState()

    var выбранныйСпособОплаты by remember { mutableStateOf<СпособОплаты?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок
        Text(
            text = "Премиум-доступ",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Информация о цене
        текущаяЦена?.let { цена ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Стоимость",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${цена.сумма} ${цена.валюта.символ}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (цена.скидка > BigDecimal.ZERO) {
                        Text(
                            text = "Скидка: ${цена.скидка}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Способы оплаты
        Text(
            text = "Выберите способ оплаты",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            СпособОплаты.values().forEach { способ ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = способ == выбранныйСпособОплаты,
                        onClick = { выбранныйСпособОплаты = способ }
                    )
                    Text(
                        text = when (способ) {
                            СпособОплаты.КАРТА -> "Банковская карта"
                            СпособОплаты.КРИПТОВАЛЮТА -> "Криптовалюта"
                            СпособОплаты.TELEGRAM_WALLET -> "Telegram Wallet"
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка покупки
        Button(
            onClick = { 
                выбранныйСпособОплаты?.let { viewModel.выполнитьПокупку(it) }
            },
            enabled = выбранныйСпособОплаты != null && !покупкаВПроцессе,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (покупкаВПроцессе) "Обработка..." else "Купить"
            )
        }

        // Статус покупки
        статусПокупки?.let { результат ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (результат.успешно)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = результат.сообщение ?: "",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    if (результат.успешно) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Полученные бонусы:",
                            style = MaterialTheme.typography.titleSmall
                        )
                        результат.полученныеБонусы.forEach { бонус ->
                            Text(
                                text = "• ${бонус.название}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
} 