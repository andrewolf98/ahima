package com.example.ahima.journal

data class JournalEntry(val date: String, val content: String)

object JournalManager {
    private val entries = mutableListOf<JournalEntry>()

    fun addEntry(entry: JournalEntry) {
        entries.add(entry)
    }

    fun getAllEntries(): List<JournalEntry> {
        return entries
    }
}
