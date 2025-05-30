package com.anima.service

import com.anima.data.*
import java.time.Duration

class MeditationService {
    fun getMeditations(): List<Meditation> {
        return listOf(
            Meditation(
                id = "med_1",
                title = "Утренняя медитация",
                description = "Начните день с осознанности и спокойствия",
                duration = 10,
                audioUrl = "meditation_morning.mp3",
                category = "Утро"
            ),
            Meditation(
                id = "med_2",
                title = "Снятие стресса",
                description = "Медитация для расслабления и снятия напряжения",
                duration = 15,
                audioUrl = "meditation_stress.mp3",
                category = "Стресс"
            ),
            Meditation(
                id = "med_3",
                title = "Глубокий сон",
                description = "Медитация для улучшения качества сна",
                duration = 20,
                audioUrl = "meditation_sleep.mp3",
                category = "Сон"
            ),
            Meditation(
                id = "med_4",
                title = "Концентрация",
                description = "Медитация для улучшения внимания и фокусировки",
                duration = 10,
                audioUrl = "meditation_focus.mp3",
                category = "Концентрация"
            )
        )
    }

    fun getSounds(): List<Sound> {
        return listOf(
            Sound(
                id = "sound_1",
                title = "Дождь",
                description = "Успокаивающий звук дождя",
                duration = 30,
                audioUrl = "rain.mp3",
                category = "Природа"
            ),
            Sound(
                id = "sound_2",
                title = "Океан",
                description = "Шум волн",
                duration = 30,
                audioUrl = "ocean.mp3",
                category = "Природа"
            ),
            Sound(
                id = "sound_3",
                title = "Лес",
                description = "Звуки леса и птиц",
                duration = 30,
                audioUrl = "forest.mp3",
                category = "Природа"
            ),
            Sound(
                id = "sound_4",
                title = "Белый шум",
                description = "Успокаивающий белый шум",
                duration = 30,
                audioUrl = "white_noise.mp3",
                category = "Шум"
            )
        )
    }

    fun getBreathingExercises(): List<BreathingExercise> {
        return listOf(
            BreathingExercise(
                id = "breath_1",
                title = "4-7-8",
                description = "Техника дыхания для снятия стресса и тревоги",
                inhaleDuration = 4,
                holdDuration = 7,
                exhaleDuration = 8,
                cycles = 4
            ),
            BreathingExercise(
                id = "breath_2",
                title = "Квадратное дыхание",
                description = "Техника для концентрации и спокойствия",
                inhaleDuration = 4,
                holdDuration = 4,
                exhaleDuration = 4,
                cycles = 5
            ),
            BreathingExercise(
                id = "breath_3",
                title = "Глубокое дыхание",
                description = "Техника для расслабления и снятия напряжения",
                inhaleDuration = 5,
                holdDuration = 2,
                exhaleDuration = 5,
                cycles = 6
            )
        )
    }

    fun getMixModes(): List<MixMode> {
        return listOf(
            MixMode(
                id = "mix_1",
                title = "Сон",
                description = "Комбинация для улучшения сна",
                meditationId = "med_3",
                soundId = "sound_4",
                duration = 30
            ),
            MixMode(
                id = "mix_2",
                title = "Концентрация",
                description = "Комбинация для фокусировки",
                meditationId = "med_4",
                soundId = "sound_1",
                duration = 20
            ),
            MixMode(
                id = "mix_3",
                title = "Снятие стресса",
                description = "Комбинация для расслабления",
                meditationId = "med_2",
                soundId = "sound_2",
                duration = 25
            )
        )
    }

    fun getRecommendedMeditation(userState: UserState): Meditation? {
        return when {
            userState.psychologicalProfile.phq9Score > 10 -> {
                getMeditations().find { it.category == "Стресс" }
            }
            userState.dailyProgress.anxiety > 7 -> {
                getMeditations().find { it.category == "Концентрация" }
            }
            userState.dailyProgress.mood == Mood.SAD -> {
                getMeditations().find { it.category == "Утро" }
            }
            else -> null
        }
    }

    fun getRecommendedSound(userState: UserState): Sound? {
        return when {
            userState.psychologicalProfile.gad7Score > 10 -> {
                getSounds().find { it.category == "Природа" }
            }
            userState.dailyProgress.anxiety > 7 -> {
                getSounds().find { it.title == "Белый шум" }
            }
            else -> null
        }
    }

    fun getRecommendedBreathingExercise(userState: UserState): BreathingExercise? {
        return when {
            userState.psychologicalProfile.gad7Score > 10 -> {
                getBreathingExercises().find { it.title == "4-7-8" }
            }
            userState.dailyProgress.anxiety > 7 -> {
                getBreathingExercises().find { it.title == "Квадратное дыхание" }
            }
            else -> null
        }
    }

    fun getRecommendedMixMode(userState: UserState): MixMode? {
        return when {
            userState.psychologicalProfile.phq9Score > 10 -> {
                getMixModes().find { it.title == "Снятие стресса" }
            }
            userState.dailyProgress.anxiety > 7 -> {
                getMixModes().find { it.title == "Концентрация" }
            }
            else -> null
        }
    }
} 