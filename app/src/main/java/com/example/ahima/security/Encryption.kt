package com.example.ahima.security

import android.util.Base64

object Encryption {

    private const val key = "demo_key_123456"

    fun encrypt(data: String): String {
        // Заглушка: в реальности здесь будет шифрование
        return Base64.encodeToString((key + data).toByteArray(), Base64.DEFAULT)
    }

    fun decrypt(data: String): String {
        // Заглушка: в реальности здесь будет расшифровка
        val decoded = String(Base64.decode(data, Base64.DEFAULT))
        return if (decoded.startsWith(key)) decoded.removePrefix(key) else "Ошибка"
    }
}
