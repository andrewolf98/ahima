import os

# Создание .gitignore
with open(".gitignore", "w") as f:
    f.write("""build/
.idea/
*.iml
*.apk
*.log
local.properties
.gradle/
.DS_Store
*.pyc
__pycache__/
""")

# Создание README.md
with open("README.md", "w", encoding="utf-8") as f:
    f.write("""# AHIMA

AHIMA — Android-приложение для психологической поддержки и самопомощи.

## Функции
- Ежедневные задания (PHQ-9, GAD-7)
- Релаксация и дыхательные практики
- ИИ-чат поддержки
- Личная аналитика и мотивация
- Импорт/экспорт профиля

## Сборка

Открыть в Android Studio и собрать под эмулятор или устройство.

```bash
./gradlew build
```

## Контакты
Разработано в сотрудничестве с ИИ. Все права открыты.
""")

# Создание strings.xml
res_path = "app/src/main/res/values"
os.makedirs(res_path, exist_ok=True)
with open(os.path.join(res_path, "strings.xml"), "w", encoding="utf-8") as f:
    f.write("""<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">AHIMA</string>
    <string name="tasks">Задания</string>
    <string name="relax">Релакс</string>
    <string name="settings">Настройки</string>
</resources>
""")

# Создание workflows для GitHub Actions
workflow_path = ".github/workflows"
os.makedirs(workflow_path, exist_ok=True)
with open(os.path.join(workflow_path, "android.yml"), "w") as f:
    f.write("""name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3
    - name: Set up JDK
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Build with Gradle
      run: ./gradlew build
""")

# Создание заглушки под SharedPreferences
prefs_path = "app/src/main/java/com/example/ahima/storage/Prefs.kt"
os.makedirs(os.path.dirname(prefs_path), exist_ok=True)
with open(prefs_path, "w", encoding="utf-8") as f:
    f.write("""package com.example.ahima.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.ahima.model.UserProfile

object Prefs {
    private const val PREFS_NAME = "ahima_prefs"

    fun saveProfile(context: Context, profile: UserProfile) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().apply {
            putInt("phq9", profile.phq9Score)
            putInt("gad7", profile.gad7Score)
            apply()
        }
    }

    fun loadProfile(context: Context): UserProfile {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return UserProfile(
            phq9Score = prefs.getInt("phq9", 0),
            gad7Score = prefs.getInt("gad7", 0)
        )
    }
}
""")


# Создание LICENSE
with open("LICENSE", "w", encoding="utf-8") as f:
    f.write("""MIT License

Copyright (c) 2025

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
""")

# Создание CONTRIBUTING.md
with open("CONTRIBUTING.md", "w", encoding="utf-8") as f:
    f.write("""# Как внести вклад

Спасибо за интерес к проекту AHIMA!

## Правила
- Перед любыми изменениями создайте issue с описанием.
- Следуйте стилю проекта и именованию.
- Все изменения отправляйте через pull request.

## Сборка
Откройте проект в Android Studio и выполните:
```bash
./gradlew build
```

## Обратная связь
Используйте GitHub Issues или напишите разработчику.
""")

# Создание gradle.properties
with open("gradle.properties", "w", encoding="utf-8") as f:
    f.write("""org.gradle.caching=true
org.gradle.parallel=true
org.gradle.configuration-cache=true
org.gradle.jvmargs=-Xmx4g
""")

print("Фикс завершён: добавлены README, LICENSE, CONTRIBUTING.md, .gitignore, strings.xml, CI workflow, SharedPreferences, UserProfile и экран профиля.")

# Создание главного экрана с навигацией
main_screen_path = "app/src/main/java/com/example/ahima/ui/MainScreen.kt"
os.makedirs(os.path.dirname(main_screen_path), exist_ok=True)
with open(main_screen_path, "w", encoding="utf-8") as f:
    f.write("""package com.example.ahima.ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.content.Context
import com.example.ahima.R

@Composable
fun MainScreen(context: Context) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf("profile", "relax", "tasks")

    Scaffold(
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Профиль") },
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate("profile")
                    }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.SelfImprovement, contentDescription = null) },
                    label = { Text("Релакс") },
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("relax")
                    }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Задания") },
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        navController.navigate("tasks")
                    }
                )
            }
        }
    ) {
        NavHost(navController, startDestination = "profile") {
            composable("profile") { ProfileScreen(context) }
            composable("relax") { Text("Релакс-зона (в разработке)") }
            composable("tasks") { Text("Задания (в разработке)") }
        }
    }
}
""")
#
# Создание модели профиля пользователя
profile_path = "app/src/main/java/com/example/ahima/model/UserProfile.kt"
os.makedirs(os.path.dirname(profile_path), exist_ok=True)
with open(profile_path, "w", encoding="utf-8") as f:
    f.write("""package com.example.ahima.model

data class UserProfile(
    val phq9Score: Int = 0,
    val gad7Score: Int = 0
)
""")

# Создание экрана профиля
profile_screen_path = "app/src/main/java/com/example/ahima/ui/ProfileScreen.kt"
os.makedirs(os.path.dirname(profile_screen_path), exist_ok=True)
with open(profile_screen_path, "w", encoding="utf-8") as f:
    f.write("""package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ahima.model.UserProfile
import com.example.ahima.storage.Prefs
import android.content.Context

@Composable
fun ProfileScreen(context: Context) {
    var phq9 by remember { mutableStateOf(0) }
    var gad7 by remember { mutableStateOf(0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Профиль пользователя", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "PHQ-9:")
        TextField(
            value = phq9.toString(),
            onValueChange = { phq9 = it.toIntOrNull() ?: 0 },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "GAD-7:")
        TextField(
            value = gad7.toString(),
            onValueChange = { gad7 = it.toIntOrNull() ?: 0 },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            Prefs.saveProfile(context, UserProfile(phq9Score = phq9, gad7Score = gad7))
        }) {
            Text("Сохранить")
        }
    }
}
""")

# Создание MainActivity.kt
main_activity_path = "app/src/main/java/com/example/ahima/MainActivity.kt"
os.makedirs(os.path.dirname(main_activity_path), exist_ok=True)
with open(main_activity_path, "w", encoding="utf-8") as f:
    f.write("""package com.example.ahima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ahima.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(context = this)
        }
    }
}
""")
import os

# Создание update_github.sh
with open("update_github.sh", "w", encoding="utf-8") as f:
    f.write("""#!/bin/bash

# Настройки Git (установите один раз, если нужно)
git config user.name "andrewolf98"
git config user.email "your_email@example.com"

# Обновим фиксированный Python-скрипт
python3 fix.py

# Добавим все изменения
git add .

# Сделаем коммит
git commit -m "Автообновление проекта через fix.py и генерацию UI"

# Отправим в GitHub
git push origin main

echo "Проект успешно обновлён и отправлен на GitHub."
""")

# Сделать файл исполняемым
os.system("chmod +x update_github.sh")
# Создание RelaxScreen.kt
relax_screen_path = "app/src/main/java/com/example/ahima/ui/RelaxScreen.kt"
os.makedirs(os.path.dirname(relax_screen_path), exist_ok=True)
with open(relax_screen_path, "w", encoding="utf-8") as f:
    f.write("""package com.example.ahima.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.Context

@Composable
fun RelaxScreen(context: Context) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Релакс-зона", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(12.dp))
        Text("Медитации, дыхательные практики и нейрозвуки — в разработке.")
    }
}
""")