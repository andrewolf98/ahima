package com.anima.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anima.data.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anima.viewmodel.SupportScreenViewModel
import com.anima.data.support.HotlineContact
import com.anima.data.support.ChatMessage
import com.anima.data.support.HandbookItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(viewModel: SupportScreenViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Экстренная помощь", "Чат с ИИ", "Справочник")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> EmergencyHelpContent(viewModel = viewModel)
            1 -> AIChatContent(viewModel = viewModel)
            2 -> HandbookContent(viewModel = viewModel)
        }
    }
}

@Composable
fun EmergencyHelpContent(viewModel: SupportScreenViewModel) {
    val hotlines by viewModel.emergencyHotlines.collectAsState()
    val countries by viewModel.countryHotlines.collectAsState()
    val selectedCountryCode by viewModel.selectedCountryCode.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box {
                Text(
                    text = "Страна: ${countries.find { it.countryCode == selectedCountryCode }?.countryName ?: "Загрузка..."}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.clickable { expanded = true }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country.countryName) },
                            onClick = {
                                viewModel.selectCountry(country.countryCode)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(hotlines) { contact ->
            EmergencyContactCard(contact = contact)
        }
    }
}

@Composable
fun EmergencyContactCard(contact: HotlineContact) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = contact.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = contact.phone,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = contact.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* TODO: Позвонить по contact.phone */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Позвонить")
            }
        }
    }
}

@Composable
fun AIChatContent(viewModel: SupportScreenViewModel = viewModel()) {
    var messageText by remember { mutableStateOf("") }
    val chatMessages by viewModel.chatMessages.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // История чата
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chatMessages) { message ->
                ChatMessageCard(message = message)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Поле ввода сообщения
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Введите сообщение...") },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = { 
                    viewModel.sendMessage(messageText)
                    messageText = ""
                },
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Отправить"
                )
            }
        }
    }
}

@Composable
fun ChatMessageCard(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .padding(4.dp)
                .widthIn(max = 300.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (message.isUser)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
fun HandbookContent(viewModel: SupportScreenViewModel = viewModel()) {
    val handbookItems by viewModel.handbookItems.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(handbookItems) { item ->
            HandbookItemCard(item = item)
        }
    }
}

@Composable
fun HandbookItemCard(item: HandbookItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (item.contacts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Контакты:",
                    style = MaterialTheme.typography.titleSmall
                )
                item.contacts.forEach { contact ->
                    Text(
                        text = contact,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

// Вспомогательные классы и функции (перенесены в SupportModels.kt)
/*
data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

data class HandbookItem(
    val title: String,
    val description: String,
    val contacts: List<String>
)

fun createMockChatHistory(): List<ChatMessage> {
    return listOf(
        ChatMessage("Здравствуйте! Как я могу вам помочь?", false),
        ChatMessage("Мне нужна помощь с тревожностью", true),
        ChatMessage("Я понимаю вашу ситуацию. Давайте обсудим, что именно вас тревожит?", false)
    )
}

fun createMockHandbookItems(): List<HandbookItem> {
    return listOf(
        HandbookItem(
            "Россия",
            "Информация о психологической помощи в России",
            listOf("8-800-333-44-34", "8-800-2000-122")
        ),
        HandbookItem(
            "США",
            "Информация о психологической помощи в США",
            listOf("1-800-273-8255", "1-800-662-4357")
        ),
        HandbookItem(
            "Европа",
            "Информация о психологической помощи в Европе",
            listOf("116 123", "0800 111 0 550")
        )
    )
}
*/