package com.anima

import android.app.Application
import com.anima.di.AppContainer

class AnimaApplication : Application() {

    // Контейнер зависимостей для всего приложения
    val appContainer = AppContainer

    override fun onCreate() {
        super.onCreate()
        // Инициализируем контейнер зависимостей с контекстом приложения
        appContainer.init(this)

        // TODO: Возможно, добавить другие инициализации (например, Crashlytics, Analytics и т.д.)
    }
} 