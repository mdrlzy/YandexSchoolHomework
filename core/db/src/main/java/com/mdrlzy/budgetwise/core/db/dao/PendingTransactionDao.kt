package com.mdrlzy.budgetwise.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mdrlzy.budgetwise.core.db.entity.PendingTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingTransactionDao {
    @Upsert
    suspend fun insert(transaction: PendingTransactionEntity): Long

    @Upsert
    suspend fun insertAll(transactions: List<PendingTransactionEntity>): List<Long>

    @Query("SELECT * FROM PendingTransactionEntity WHERE localId = :localId LIMIT 1")
    suspend fun findByLocalId(localId: Long): PendingTransactionEntity?

    @Query("SELECT * FROM PendingTransactionEntity WHERE remoteId = :remoteId LIMIT 1")
    suspend fun findByRemoteId(remoteId: Long): PendingTransactionEntity?

    @Query("SELECT * FROM PendingTransactionEntity")
    suspend fun getAll(): List<PendingTransactionEntity>

    @Query("SELECT * FROM PendingTransactionEntity")
    fun allFlow(): Flow<List<PendingTransactionEntity>>

    @Query("DELETE FROM PendingTransactionEntity WHERE localId = :localId")
    suspend fun delete(localId: Long)
}