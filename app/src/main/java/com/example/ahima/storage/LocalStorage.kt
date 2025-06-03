package com.example.ahima.storage

import com.example.ahima.model.UserProfile

object LocalStorage {
    fun getProfile(): UserProfile {
        return UserProfile(phq9Score = 10, gad7Score = 7)
    }
}
