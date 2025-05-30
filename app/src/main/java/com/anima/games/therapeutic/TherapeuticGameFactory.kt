package com.anima.games.therapeutic

import com.anima.data.therapeutic.TherapeuticGameConfig
import com.anima.data.therapeutic.TherapeuticGameType
import com.anima.games.therapeutic.games.*

object TherapeuticGameFactory {
    fun createGame(type: TherapeuticGameType, config: TherapeuticGameConfig): TherapeuticGameEngine {
        return when (type) {
            TherapeuticGameType.DRAGON_BREATHING -> DragonBreathingGame(config)
            TherapeuticGameType.THOUGHT_GARDEN -> ThoughtGardenGame(config)
            TherapeuticGameType.EMOTION_PUZZLE -> EmotionPuzzleGame(config)
            TherapeuticGameType.SOCIAL_MAZE -> SocialMazeGame(config)
            TherapeuticGameType.SOUND_DETECTIVE -> SoundDetectiveGame(config)
            TherapeuticGameType.RHYTHM_MEDITATION -> RhythmMeditationGame(config)
            TherapeuticGameType.MEMORY_ISLAND -> MemoryIslandGame(config)
            TherapeuticGameType.THOUGHT_BALANCE -> ThoughtBalanceGame(config)
            TherapeuticGameType.COLOR_THERAPY -> ColorTherapyGame(config)
            TherapeuticGameType.KINDNESS_QUEST -> KindnessQuestGame(config)
        }
    }
} 