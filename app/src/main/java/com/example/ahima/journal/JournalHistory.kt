package com.example.ahima.journal

object JournalHistory {
    fun getSummary(): String {
        return JournalManager.getAllEntries().joinToString("\n") { "${it.date}: ${it.content}" }
    }
}
