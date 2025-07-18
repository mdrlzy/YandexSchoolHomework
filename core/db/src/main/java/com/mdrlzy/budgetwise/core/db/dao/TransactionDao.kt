package com.mdrlzy.budgetwise.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mdrlzy.budgetwise.core.db.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Upsert
    suspend fun insert(transaction: TransactionEntity): Long

    @Upsert
    suspend fun insertAll(transactions: List<TransactionEntity>): List<Long>

    @Query("SELECT * FROM TransactionEntity WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): TransactionEntity?

    @Query("SELECT * FROM TransactionEntity")
    suspend fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM TransactionEntity")
    fun allFlow(): Flow<List<TransactionEntity>>

    @Query("DELETE FROM TransactionEntity WHERE id = :id")
    suspend fun delete(id: Long)
}