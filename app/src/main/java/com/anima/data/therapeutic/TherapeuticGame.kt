package com.anima.data.therapeutic

enum class TherapeuticGameType {
    DRAGON_BREATHING,    // Дыхание дракона
    THOUGHT_GARDEN,      // Сад мыслей
    EMOTION_PUZZLE,      // Эмоциональный пазл
    SOCIAL_MAZE,         // Лабиринт социальных ситуаций
    SOUND_DETECTIVE,     // Звуковой детектив
    RHYTHM_MEDITATION,   // Ритм-медитация
    MEMORY_ISLAND,       // Остров воспоминаний
    THOUGHT_BALANCE,     // Баланс мыслей
    COLOR_THERAPY,       // Цветотерапия
    KINDNESS_QUEST       // Квест доброты
}

data class TherapeuticGameConfig(
    val type: TherapeuticGameType,
    val duration: Long, // в миллисекундах
    val difficulty: Int, // 1-5
    val targetScore: Int,
    val therapeuticGoal: String
)

interface TherapeuticGameEngine {
    fun start()
    fun pause()
    fun resume()
    fun stop()
    fun getScore(): Int
    fun getProgress(): Float // 0.0 - 1.0
    fun getTimeSpent(): Long
    fun isFinished(): Boolean
    fun getTherapeuticFeedback(): String
}

// Конфигурации для каждой игры
object TherapeuticGameConfigs {
    val dragonBreathing = TherapeuticGameConfig(
        type = TherapeuticGameType.DRAGON_BREATHING,
        duration = 300000L, // 5 минут
        difficulty = 2,
        targetScore = 10,
        therapeuticGoal = "Обучение диафрагмальному дыханию (4-7-8 техника)"
    )

    val thoughtGarden = TherapeuticGameConfig(
        type = TherapeuticGameType.THOUGHT_GARDEN,
        duration = 600000L, // 10 минут
        difficulty = 3,
        targetScore = 15,
        therapeuticGoal = "Когнитивно-поведенческая терапия — переключение с навязчивых мыслей"
    )

    val emotionPuzzle = TherapeuticGameConfig(
        type = TherapeuticGameType.EMOTION_PUZZLE,
        duration = 300000L, // 5 минут
        difficulty = 2,
        targetScore = 8,
        therapeuticGoal = "Развитие распознавания эмоций"
    )

    val socialMaze = TherapeuticGameConfig(
        type = TherapeuticGameType.SOCIAL_MAZE,
        duration = 600000L, // 10 минут
        difficulty = 4,
        targetScore = 12,
        therapeuticGoal = "Тренировка социальных навыков"
    )

    val soundDetective = TherapeuticGameConfig(
        type = TherapeuticGameType.SOUND_DETECTIVE,
        duration = 300000L, // 5 минут
        difficulty = 3,
        targetScore = 10,
        therapeuticGoal = "Развитие селективного внимания"
    )

    val rhythmMeditation = TherapeuticGameConfig(
        type = TherapeuticGameType.RHYTHM_MEDITATION,
        duration = 300000L, // 5 минут
        difficulty = 2,
        targetScore = 20,
        therapeuticGoal = "Снижение кортизола через ритмичные действия"
    )

    val memoryIsland = TherapeuticGameConfig(
        type = TherapeuticGameType.MEMORY_ISLAND,
        duration = 600000L, // 10 минут
        difficulty = 2,
        targetScore = 10,
        therapeuticGoal = "Активация позитивных воспоминаний"
    )

    val thoughtBalance = TherapeuticGameConfig(
        type = TherapeuticGameType.THOUGHT_BALANCE,
        duration = 300000L, // 5 минут
        difficulty = 4,
        targetScore = 15,
        therapeuticGoal = "Тренировка эмоционального баланса"
    )

    val colorTherapy = TherapeuticGameConfig(
        type = TherapeuticGameType.COLOR_THERAPY,
        duration = 600000L, // 10 минут
        difficulty = 1,
        targetScore = 5,
        therapeuticGoal = "Снижение симпатической активации"
    )

    val kindnessQuest = TherapeuticGameConfig(
        type = TherapeuticGameType.KINDNESS_QUEST,
        duration = 86400000L, // 24 часа
        difficulty = 3,
        targetScore = 10,
        therapeuticGoal = "Формирование позитивных привычек"
    )
} 