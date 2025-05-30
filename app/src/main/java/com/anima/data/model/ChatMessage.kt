package com.anima.data.model

import java.time.LocalDateTime

data class ChatMessage(
    val id: String,
    val content: String,
    val timestamp: LocalDateTime,
    val isFromUser: Boolean
) 