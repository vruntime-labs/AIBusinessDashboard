package com.aibusiness.dashboard.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.aibusiness.dashboard.data.model.GenerationHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM generation_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<GenerationHistoryEntity>>

    @Query("SELECT * FROM generation_history WHERE id = :id")
    suspend fun getById(id: String): GenerationHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GenerationHistoryEntity)

    @Query("DELETE FROM generation_history WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM generation_history")
    suspend fun clearAll()
}

@Database(
    entities = [GenerationHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}
