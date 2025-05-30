package com.anima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anima.data.support.CountryHotlines
import com.anima.data.support.HotlineContact
import com.anima.data.support.getMockCountryHotlines
import com.anima.data.settings.UserSettingsRepository
import com.anima.data.support.ChatMessage
import com.anima.data.support.HandbookItem
import com.anima.data.support.createMockHandbookItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class SupportScreenViewModel(private val userSettingsRepository: UserSettingsRepository = UserSettingsRepository()) : ViewModel() {

    private val _countryHotlines = MutableStateFlow<List<CountryHotlines>>(emptyList())
    val countryHotlines: StateFlow<List<CountryHotlines>> = _countryHotlines.asStateFlow()

    private val _selectedCountryCode = MutableStateFlow<String>("RU") // Страна по умолчанию, будет обновлена из настроек
    val selectedCountryCode: StateFlow<String> = _selectedCountryCode.asStateFlow()

    val emergencyHotlines: StateFlow<List<HotlineContact>> = MutableStateFlow(emptyList())

    // Состояние чата с ИИ
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Состояние справочника
    private val _handbookItems = MutableStateFlow<List<HandbookItem>>(emptyList())
    val handbookItems: StateFlow<List<HandbookItem>> = _handbookItems.asStateFlow()

    init {
        loadCountryHotlines()
        loadUserSettings()
        viewModelScope.launch {
            selectedCountryCode.collect {
                updateEmergencyHotlines(it)
            }
        }
        loadMockChatHistory() // Загружаем моковую историю чата при инициализации
        loadHandbookItems() // Загружаем элементы справочника при инициализации
    }

    private fun loadCountryHotlines() {
        viewModelScope.launch {
            // TODO: Загрузка данных о горячих линиях по странам из репозитория или другого источника
            _countryHotlines.value = getMockCountryHotlines()
        }
    }

    private fun loadUserSettings() {
        viewModelScope.launch {
            val settings = userSettingsRepository.getUserSettings()
            _selectedCountryCode.value = settings.countryCode
        }
    }

    fun selectCountry(countryCode: String) {
        _selectedCountryCode.value = countryCode
    }

    private fun updateEmergencyHotlines(countryCode: String) {
        val hotlines = _countryHotlines.value.find { it.countryCode == countryCode }?.hotlines ?: emptyList()
        (emergencyHotlines as MutableStateFlow).value = hotlines
    }

    // Функции для управления чатом с ИИ
    private fun loadMockChatHistory() {
        _chatMessages.value = listOf(
            ChatMessage("Здравствуйте! Как я могу вам помочь?", false),
            ChatMessage("Мне нужна помощь с тревожностью", true),
            ChatMessage("Я понимаю вашу ситуацию. Давайте обсудим, что именно вас тревожит?", false)
        )
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val newUserMessage = ChatMessage(text, true)
        _chatMessages.value = _chatMessages.value + newUserMessage

        // Имитация ответа AI
        viewModelScope.launch {
            delay(1000) // Имитируем задержку ответа
            val aiResponse = generateMockAiResponse(text)
            val newAiMessage = ChatMessage(aiResponse, false)
            _chatMessages.value = _chatMessages.value + newAiMessage
        }
    }

    private fun generateMockAiResponse(userMessage: String): String {
        return when {
            userMessage.contains("тревожностью", ignoreCase = true) -> "Пожалуйста, расскажите подробнее о вашей тревожности. Когда она началась? Что ее вызывает?"
            userMessage.contains("помощь", ignoreCase = true) -> "Я здесь, чтобы выслушать вас и предложить поддержку. Поделитесь, что вас беспокоит."
            else -> "Спасибо за ваше сообщение. Я обрабатываю информацию и скоро отвечу."
        }
    }

    // Функции для управления справочником
    private fun loadHandbookItems() {
        viewModelScope.launch {
            // TODO: Загрузка данных справочника из репозитория или другого источника
            _handbookItems.value = createMockHandbookItems()
        }
    }

    // TODO: Добавить функции для управления настройками приватности/безопасности
} 