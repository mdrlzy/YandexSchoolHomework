package com.mdrlzy.budgetwise.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mdrlzy.budgetwise.core.db.entity.AccountEntity
import com.mdrlzy.budgetwise.core.db.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Upsert
    suspend fun insert(category: CategoryEntity): Long

    @Query("SELECT * FROM CategoryEntity")
    suspend fun getAll(): List<CategoryEntity>

    @Query("SELECT * FROM CategoryEntity")
    fun allFlow(): Flow<List<CategoryEntity>>
}