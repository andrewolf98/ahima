package com.anima.di

import android.content.Context
import com.anima.data.db.AppDatabase
import com.anima.data.db.DailyProgressDao
import com.anima.data.db.DiagnosticEventDao
import com.anima.data.db.GameEventDao
import com.anima.data.db.RelaxationEventDao
import com.anima.data.db.GeneratedContentConfigDao
import com.anima.service.AnalyticsService

// Простой контейнер зависимостей для приложения.
// В реальном приложении рекомендуется использовать фреймворки для внедрения зависимостей, такие как Dagger/Hilt.
object AppContainer {

    // lateinit var используется, потому что контекст будет инициализирован позже в Application классе.
    private lateinit var appContext: Context

    // Инициализация контейнера с контекстом приложения.
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // Ленивая инициализация базы данных Room.
    private val database: AppDatabase by lazy {
        // Проверяем, что контекст был инициализирован
        if (!this::appContext.isInitialized) {
            throw IllegalStateException("AppContainer not initialized. Call init() first.")
        }
        AppDatabase.getDatabase(appContext)
    }

    // Ленивая инициализация DAO, используя экземпляр базы данных.
    val dailyProgressDao: DailyProgressDao by lazy {
        database.dailyProgressDao()
    }

    val diagnosticEventDao: DiagnosticEventDao by lazy {
        database.diagnosticEventDao()
    }

    val gameEventDao: GameEventDao by lazy {
        database.gameEventDao()
    }

    val relaxationEventDao: RelaxationEventDao by lazy {
        database.relaxationEventDao()
    }

    val generatedContentConfigDao: GeneratedContentConfigDao by lazy {
        database.generatedContentConfigDao()
    }

    // Ленивая инициализация AnalyticsService, передавая ему DAO как зависимости.
    val analyticsService: AnalyticsService by lazy {
        AnalyticsService(
            dailyProgressDao = dailyProgressDao,
            diagnosticEventDao = diagnosticEventDao,
            gameEventDao = gameEventDao,
            relaxationEventDao = relaxationEventDao,
            generatedContentConfigDao = generatedContentConfigDao
        )
    }

    // TODO: Добавить другие сервисы, репозитории и т.д., которые будут управляться контейнером.
    // Например, DiagnosticService (хотя сейчас он не имеет зависимостей от базы данных)
    // val diagnosticService: DiagnosticService by lazy { DiagnosticService() }
} 