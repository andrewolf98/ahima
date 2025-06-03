package com.example.ahima.support

object AiSupportChat {
    fun processUserMessage(message: String): String {
        return when {
            message.contains("тревога", ignoreCase = true) -> "Попробуйте дыхание по схеме 4-7-8 и тёплый чай."
            message.contains("паника", ignoreCase = true) -> "Сконцентрируйтесь на дыхании и заземлении."
            else -> "Я здесь, чтобы поддержать вас. Расскажите подробнее."
        }
    }
}
