package com.anima.data.diagnostic

import java.time.LocalDateTime

// Базовая модель диагностической мини-игры
data class DiagnosticGame(
    val id: String, // Уникальный идентификатор игры
    val название: String,
    val описание: String,
    val тип: ТипИгры,
    val сложность: СложностьИгры,
    val длительностьСек: Int, // Длительность в секундах
    val параметры: Map<String, Any> // Специфичные параметры для каждой игры
)

// Перечисление типов мини-игр
enum class ТипИгры {
    ЭМОЦИОНАЛЬНЫЙ_СОРТЕР,
    СТРУП_ТЕСТ,
    ПАМЯТЬ_ЧИСЕЛ,
    ПОИСК_ОТЛИЧИЙ,
    АССОЦИАЦИИ_СЛОВ,
    ВРЕМЯ_РЕАКЦИИ,
    СОРТИРОВКА_КАРТОЧЕК,
    РАСПОЗНАВАНИЕ_ИНТОНАЦИЙ,
    ПРОСТРАНСТВЕННЫЙ_ТЕСТ,
    FLANKER_TASK
}

// Сложность игры
enum class СложностьИгры {
    ЛЕГКО,
    СРЕДНЕ,
    СЛОЖНО,
    АДАПТИВНО
}

// Пример конфигурации для Эмоционального сортера
data class КонфигЭмоциональногоСортера(
    val эмоции: List<Эмоция>,
    val лимитВремениСек: Int,
    val минимальнаяТочность: Double
)

data class Эмоция(
    val название: String,
    val emoji: String,
    val urlКартинки: String? = null
)

// Пример конфигурации для Струп-теста
data class КонфигСтрупТеста(
    val слова: List<String>,
    val цвета: List<String>,
    val количествоРаундов: Int,
    val лимитВремениСек: Int
)

// Пример конфигурации для Памяти чисел
data class КонфигПамятиЧисел(
    val длинаПоследовательности: Int,
    val диапазонЦифр: IntRange,
    val количествоРаундов: Int
)

// Пример конфигурации для Поиска отличий
data class КонфигПоискаОтличий(
    val парыКартинок: List<ПараКартинок>,
    val лимитВремениСек: Int,
    val минимальноеКоличествоОтличий: Int
)

data class ПараКартинок(
    val id: String,
    val url1: String,
    val url2: String,
    val отличия: List<Отличие>
)

data class Отличие(
    val id: String,
    val описание: String
)

// Пример конфигурации для Ассоциаций слов
data class КонфигАссоциацийСлов(
    val пары: List<ПараСлов>,
    val лимитВремениСек: Int
)

data class ПараСлов(
    val слово: String,
    val ассоциации: List<String>
)

// Пример конфигурации для Времени реакции
data class КонфигВремениРеакции(
    val количествоРаундов: Int,
    val минимальныйИнтервалМс: Int,
    val максимальныйИнтервалМс: Int
)

// Пример конфигурации для Сортировки карточек
data class КонфигСортировкиКарточек(
    val карточки: List<Карточка>,
    val правила: List<ПравилоСортировки>,
    val количествоРаундов: Int
)

data class Карточка(
    val id: String,
    val атрибуты: Map<String, String>
)

data class ПравилоСортировки(
    val id: String,
    val описание: String
)

// Пример конфигурации для Распознавания интонаций
data class КонфигРаспознаванияИнтонаций(
    val аудиофайлы: List<АудиоКлип>,
    val эмоции: List<Эмоция>,
    val лимитВремениСек: Int
)

data class АудиоКлип(
    val id: String,
    val url: String,
    val эмоция: Эмоция
)

// Пример конфигурации для Пространственного теста
data class КонфигПространственногоТеста(
    val узоры: List<Узор>,
    val лимитВремениСек: Int
)

data class Узор(
    val id: String,
    val url: String
)

// Пример конфигурации для Flanker Task
data class КонфигFlankerTask(
    val количествоРаундов: Int,
    val лимитВремениСек: Int
) 