package com.anima.data.repository

import com.anima.data.database.GameResultDao
import com.anima.data.database.GameResultEntity
import com.anima.data.diagnostic.ТипИгры
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class GameResultRepository(private val gameResultDao: GameResultDao) {
    
    fun getAllResults(): Flow<List<GameResultEntity>> = flow {
        emit(gameResultDao.getAllResults())
    }
    
    fun getResultsByGameType(gameType: ТипИгры): Flow<List<GameResultEntity>> = flow {
        emit(gameResultDao.getResultsByGameType(gameType))
    }
    
    suspend fun saveResult(
        gameType: ТипИгры,
        score: Int,
        total: Int,
        timeSpent: Long,
        mistakes: Int
    ) {
        val result = GameResultEntity(
            gameType = gameType,
            score = score,
            total = total,
            timeSpent = timeSpent,
            mistakes = mistakes,
            timestamp = Date()
        )
        gameResultDao.insertResult(result)
    }
    
    suspend fun deleteResult(result: GameResultEntity) {
        gameResultDao.deleteResult(result)
    }
    
    suspend fun deleteAllResults() {
        gameResultDao.deleteAllResults()
    }
    
    suspend fun getAverageScore(gameType: ТипИгры): Double {
        val results = gameResultDao.getResultsByGameType(gameType)
        return if (results.isEmpty()) 0.0
        else results.map { it.score.toDouble() / it.total }.average()
    }
    
    suspend fun getBestScore(gameType: ТипИгры): Int {
        val results = gameResultDao.getResultsByGameType(gameType)
        return if (results.isEmpty()) 0
        else results.maxOf { it.score }
    }
    
    suspend fun getAverageTime(gameType: ТипИгры): Long {
        val results = gameResultDao.getResultsByGameType(gameType)
        return if (results.isEmpty()) 0
        else results.map { it.timeSpent }.average().toLong()
    }
    
    suspend fun getTotalGamesPlayed(gameType: ТипИгры): Int {
        return gameResultDao.getResultsByGameType(gameType).size
    }
    
    suspend fun getMistakesDistribution(gameType: ТипИгры): Map<Int, Int> {
        val results = gameResultDao.getResultsByGameType(gameType)
        return results.groupingBy { it.mistakes }.eachCount()
    }
    
    suspend fun getProgressOverTime(gameType: ТипИгры): List<Pair<Date, Double>> {
        val results = gameResultDao.getResultsByGameType(gameType)
        return results.map { 
            Pair(it.timestamp, it.score.toDouble() / it.total)
        }.sortedBy { it.first }
    }
} 