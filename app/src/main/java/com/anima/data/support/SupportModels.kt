package com.anima.data.support

data class HotlineContact(
    val title: String,
    val phone: String,
    val description: String
)

data class CountryHotlines(
    val countryCode: String,
    val countryName: String,
    val hotlines: List<HotlineContact>
)

// Моковые данные для примера
fun getMockCountryHotlines(): List<CountryHotlines> {
    return listOf(
        CountryHotlines(
            countryCode = "RU",
            countryName = "Россия",
            hotlines = listOf(
                HotlineContact(
                    title = "Экстренная психологическая помощь",
                    phone = "8-800-333-44-34",
                    description = "Круглосуточная бесплатная помощь"
                ),
                HotlineContact(
                    title = "Телефон доверия",
                    phone = "8-800-2000-122",
                    description = "Единый общероссийский номер"
                ),
                HotlineContact(
                    title = "Скорая помощь",
                    phone = "103",
                    description = "Медицинская помощь"
                ),
                 HotlineContact(
                    title = "Полиция",
                    phone = "102",
                    description = "Экстренная помощь"
                )
            )
        ),
        CountryHotlines(
            countryCode = "US",
            countryName = "США",
            hotlines = listOf(
                HotlineContact(
                    title = "National Suicide Prevention Lifeline",
                    phone = "1-800-273-8255",
                    description = "24/7, free and confidential support"
                ),
                HotlineContact(
                    title = "Crisis Text Line",
                    phone = "Text HOME to 741741",
                    description = "Support for people in crisis"
                )
            )
        )
        // Добавьте данные для других стран здесь
    )
} 