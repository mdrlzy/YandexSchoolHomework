package com.mdrlzy.budgetwise.core.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mdrlzy.budgetwise.core.db.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Upsert
    suspend fun insert(account: AccountEntity): Long

    @Query("SELECT * FROM AccountEntity")
    suspend fun getAll(): List<AccountEntity>

    @Query("SELECT * FROM AccountEntity")
    fun allFlow(): Flow<List<AccountEntity>>
}