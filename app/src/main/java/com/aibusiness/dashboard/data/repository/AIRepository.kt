package com.aibusiness.dashboard.data.repository

import android.content.Context
import androidx.room.Room
import com.aibusiness.dashboard.data.local.AppDatabase
import com.aibusiness.dashboard.data.model.*
import com.aibusiness.dashboard.data.remote.AIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AIRepository(context: Context) {

    private val database = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "ai_business_dashboard.db"
    ).build()

    private val historyDao = database.historyDao()
    private val aiService = AIService()

    fun getHistory(): Flow<List<GenerationResult>> {
        return historyDao.getAllHistory().map { entities ->
            entities.map { it.toResult() }
        }
    }

    suspend fun generate(
        request: GenerationRequest,
        apiKey: String? = null
    ): GenerationResult {
        val result = aiService.generate(request, apiKey)
        
        // Save to history
        historyDao.insert(result.toEntity())
        
        return result
    }

    suspend fun deleteHistoryItem(id: String) {
        historyDao.delete(id)
    }

    suspend fun clearHistory() {
        historyDao.clearAll()
    }

    private fun GenerationHistoryEntity.toResult(): GenerationResult {
        return GenerationResult(
            id = id,
            request = GenerationRequest(
                prompt = prompt,
                format = GenerationFormat.valueOf(format),
                style = GenerationStyle.valueOf(style),
                theme = GenerationTheme.valueOf(theme),
                type = GenerationType.valueOf(type)
            ),
            content = content,
            imageUrl = imageUrl,
            timestamp = timestamp,
            isSuccess = isSuccess
        )
    }

    private fun GenerationResult.toEntity(): GenerationHistoryEntity {
        return GenerationHistoryEntity(
            id = id,
            prompt = request.prompt,
            format = request.format.name,
            style = request.style.name,
            theme = request.theme.name,
            type = request.type.name,
            content = content,
            imageUrl = imageUrl,
            timestamp = timestamp,
            isSuccess = isSuccess
        )
    }
}
