package com.example.ahima.data

data class SupportContact(
    val country: String,
    val emergencyNumber: String,
    val mentalHealthLine: String
)

object CountryData {
    private val contacts = listOf(
        SupportContact("Россия", "112", "8-800-2000-122"),
        SupportContact("США", "911", "988"),
        SupportContact("Германия", "112", "0800 1110111")
    )

    fun getContactFor(country: String): SupportContact? {
        return contacts.find { it.country == country }
    }
}
