package com.anima.data.support

// Вспомогательные классы и функции для справочника
data class HandbookItem(
    val title: String,
    val description: String,
    val contacts: List<String>
)

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
        // Добавьте другие элементы справочника здесь
    )
} 