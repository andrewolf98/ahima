package com.anima.data.diagnostic

import java.time.LocalDateTime

// Базовые модели для диагностики
data class DiagnosticTest(
    val id: String,
    val title: String,
    val description: String,
    val category: TestCategory,
    val questions: List<DiagnosticQuestion>,
    val scoringSystem: ScoringSystem,
    val estimatedDuration: Int, // in minutes
    val frequency: TestFrequency,
    val prerequisites: List<String> = emptyList(),
    val contraindications: List<String> = emptyList()
)

enum class TestCategory {
    DEPRESSION,
    ANXIETY,
    PTSD,
    COGNITIVE,
    QUALITY_OF_LIFE,
    SLEEP,
    STRESS,
    SOCIAL,
    PERSONALITY
}

data class DiagnosticQuestion(
    val id: String,
    val text: String,
    val options: List<QuestionOption>,
    val category: QuestionCategory,
    val weight: Float = 1.0f,
    val isRequired: Boolean = true,
    val dependsOn: String? = null,
    val category: QuestionCategory
)

data class QuestionOption(
    val value: Int,
    val text: String,
    val description: String?
)

enum class QuestionCategory {
    MOOD,
    ANXIETY,
    SLEEP,
    COGNITION,
    SOCIAL,
    PHYSICAL
}

data class ScoringSystem(
    val minScore: Int,
    val maxScore: Int,
    val thresholds: List<ScoreThreshold>,
    val interpretation: Map<String, String>
)

data class ScoreThreshold(
    val min: Int,
    val max: Int,
    val level: String,
    val recommendations: List<String>
)

// Специфические тесты
data class PCL5Test(
    val id: String = "pcl5",
    val title: String = "PCL-5 (Шкала ПТСР)",
    val description: String = "Оценка симптомов посттравматического стрессового расстройства",
    val questions: List<PCL5Question>,
    val clusters: List<PCL5Cluster>
)

data class PCL5Question(
    val id: String,
    val text: String,
    val cluster: PCL5Cluster,
    val options: List<QuestionOption>
)

enum class PCL5Cluster {
    INTRUSION,
    AVOIDANCE,
    NEGATIVE_ALTERATIONS,
    AROUSAL
}

data class BeckDepressionTest(
    val id: String = "beck",
    val title: String = "Шкала депрессии Бека",
    val description: String = "Оценка тяжести депрессивных симптомов",
    val questions: List<BeckQuestion>
)

data class BeckQuestion(
    val id: String,
    val text: String,
    val options: List<QuestionOption>
)

data class CognitiveDistortionTest(
    val id: String = "cognitive",
    val title: String = "Тест когнитивных искажений",
    val description: String = "Выявление паттернов негативного мышления",
    val questions: List<CognitiveQuestion>,
    val distortionTypes: List<DistortionType>
)

data class CognitiveQuestion(
    val id: String,
    val text: String,
    val distortionType: DistortionType,
    val options: List<QuestionOption>
)

enum class DistortionType {
    ALL_OR_NOTHING,
    OVERGENERALIZATION,
    MENTAL_FILTER,
    DISQUALIFYING_POSITIVE,
    JUMPING_TO_CONCLUSIONS,
    MAGNIFICATION,
    EMOTIONAL_REASONING,
    SHOULD_STATEMENTS,
    LABELING,
    PERSONALIZATION
}

data class QualityOfLifeTest(
    val id: String = "qol",
    val title: String = "Оценка качества жизни",
    val description: String = "Комплексная оценка различных аспектов жизни",
    val domains: List<LifeDomain>,
    val questions: List<QualityOfLifeQuestion>
)

data class QualityOfLifeQuestion(
    val id: String,
    val text: String,
    val domain: LifeDomain,
    val options: List<QuestionOption>
)

enum class LifeDomain {
    PHYSICAL,
    PSYCHOLOGICAL,
    SOCIAL,
    ENVIRONMENTAL,
    SPIRITUAL
}

// Результаты диагностики
data class DiagnosticResult(
    val testId: String,
    val userId: String,
    val date: LocalDateTime,
    val scores: Map<String, Int>,
    val interpretation: String,
    val recommendations: List<String>,
    val riskLevel: RiskLevel,
    val trends: List<DiagnosticTrend> = emptyList(),
    val comparativeAnalysis: ComparativeAnalysis? = null,
    val detailedBreakdown: Map<String, DetailedScore> = emptyMap()
)

enum class RiskLevel {
    LOW,
    MODERATE,
    HIGH,
    SEVERE
}

// Новые модели для расширенной диагностики
data class SleepTest(
    val id: String = "sleep",
    val title: String = "Оценка качества сна",
    val description: String = "Комплексная оценка качества сна и связанных с ним проблем",
    val questions: List<SleepQuestion>,
    val sleepDomains: List<SleepDomain>
)

data class SleepQuestion(
    val id: String,
    val text: String,
    val domain: SleepDomain,
    val options: List<QuestionOption>
)

enum class SleepDomain {
    SLEEP_QUALITY,
    SLEEP_LATENCY,
    SLEEP_DURATION,
    SLEEP_EFFICIENCY,
    SLEEP_DISTURBANCES,
    SLEEP_MEDICATION,
    DAYTIME_DYSFUNCTION
}

data class StressTest(
    val id: String = "stress",
    val title: String = "Оценка уровня стресса",
    val description: String = "Определение уровня стресса и его влияния на различные сферы жизни",
    val questions: List<StressQuestion>,
    val stressDomains: List<StressDomain>
)

data class StressQuestion(
    val id: String,
    val text: String,
    val domain: StressDomain,
    val options: List<QuestionOption>
)

enum class StressDomain {
    PHYSICAL,
    EMOTIONAL,
    COGNITIVE,
    BEHAVIORAL,
    SOCIAL,
    WORK
}

data class SocialTest(
    val id: String = "social",
    val title: String = "Оценка социального функционирования",
    val description: String = "Анализ социальных навыков и качества межличностных отношений",
    val questions: List<SocialQuestion>,
    val socialDomains: List<SocialDomain>
)

data class SocialQuestion(
    val id: String,
    val text: String,
    val domain: SocialDomain,
    val options: List<QuestionOption>
)

enum class SocialDomain {
    RELATIONSHIPS,
    COMMUNICATION,
    EMPATHY,
    SOCIAL_ANXIETY,
    SUPPORT_SYSTEM,
    CONFLICT_RESOLUTION
}

data class DiagnosticTrend(
    val metric: String,
    val previousValue: Int,
    val currentValue: Int,
    val change: Double,
    val significance: TrendSignificance
)

enum class TrendSignificance {
    SIGNIFICANT_IMPROVEMENT,
    SLIGHT_IMPROVEMENT,
    NO_CHANGE,
    SLIGHT_DETERIORATION,
    SIGNIFICANT_DETERIORATION
}

data class ComparativeAnalysis(
    val populationAverage: Double,
    val percentile: Int,
    val ageGroupComparison: Map<String, Double>,
    val genderComparison: Map<String, Double>
)

data class DetailedScore(
    val rawScore: Int,
    val normalizedScore: Double,
    val percentile: Int,
    val interpretation: String,
    val subScores: Map<String, Int> = emptyMap()
) 