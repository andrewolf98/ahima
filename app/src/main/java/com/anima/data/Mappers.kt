package com.anima.data

import com.anima.data.UserState.*
import java.time.LocalDateTime
import java.util.UUID

object UserMapper {
    fun toEntity(user: UserState): UserEntity {
        return UserEntity(
            id = user.id,
            name = user.name,
            age = user.age,
            country = user.country,
            depressionScore = user.psychologicalProfile.depressionScore,
            anxietyScore = user.psychologicalProfile.anxietyScore,
            lastTestDate = user.psychologicalProfile.lastTestDate,
            diagnosis = user.psychologicalProfile.diagnosis
        )
    }

    fun toDomain(entity: UserEntity): UserState {
        return UserState(
            id = entity.id,
            name = entity.name,
            age = entity.age,
            country = entity.country,
            psychologicalProfile = PsychologicalProfile(
                depressionScore = entity.depressionScore,
                anxietyScore = entity.anxietyScore,
                lastTestDate = entity.lastTestDate,
                diagnosis = entity.diagnosis,
                riskFactors = emptyList() // TODO: Добавить маппинг факторов риска
            ),
            dailyProgress = DailyProgress(
                completedTasks = 0,
                mood = Mood.NEUTRAL,
                anxietyLevel = 0,
                productivity = 0,
                meditationMinutes = 0,
                sleepHours = 0f
            ),
            settings = UserSettings(
                notificationsEnabled = true,
                darkTheme = false,
                language = "ru",
                syncEnabled = true,
                analyticsEnabled = true,
                soundVolume = 1f,
                autoPlay = true,
                backgroundPlay = true
            )
        )
    }
}

object DailyProgressMapper {
    fun toEntity(progress: DailyProgress, userId: String): DailyProgressEntity {
        return DailyProgressEntity(
            id = UUID.randomUUID().toString(),
            userId = userId,
            date = LocalDateTime.now(),
            completedTasks = progress.completedTasks,
            mood = progress.mood.name,
            anxietyLevel = progress.anxietyLevel,
            productivity = progress.productivity,
            meditationMinutes = progress.meditationMinutes,
            sleepHours = progress.sleepHours
        )
    }

    fun toDomain(entity: DailyProgressEntity): DailyProgress {
        return DailyProgress(
            completedTasks = entity.completedTasks,
            mood = Mood.valueOf(entity.mood),
            anxietyLevel = entity.anxietyLevel,
            productivity = entity.productivity,
            meditationMinutes = entity.meditationMinutes,
            sleepHours = entity.sleepHours
        )
    }
}

object TestResultMapper {
    fun toEntity(result: TestResult, userId: String): TestResultEntity {
        return TestResultEntity(
            id = UUID.randomUUID().toString(),
            userId = userId,
            testId = result.testId,
            score = result.score,
            severity = result.severity,
            recommendations = result.recommendations.joinToString("\n"),
            date = result.date
        )
    }

    fun toDomain(entity: TestResultEntity): TestResult {
        return TestResult(
            testId = entity.testId,
            score = entity.score,
            severity = entity.severity,
            recommendations = entity.recommendations.split("\n"),
            date = entity.date
        )
    }
}

object AchievementMapper {
    fun toEntity(achievement: Achievement, userId: String): AchievementEntity {
        return AchievementEntity(
            id = UUID.randomUUID().toString(),
            userId = userId,
            title = achievement.title,
            description = achievement.description,
            type = achievement.type.name,
            date = achievement.date
        )
    }

    fun toDomain(entity: AchievementEntity): Achievement {
        return Achievement(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            type = AchievementType.valueOf(entity.type),
            date = entity.date
        )
    }
} 