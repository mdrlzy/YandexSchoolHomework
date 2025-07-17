package com.mdrlzy.budgetwise.core.db

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mdrlzy.budgetwise.core.db.dao.AccountDao
import com.mdrlzy.budgetwise.core.db.dao.CategoryDao
import com.mdrlzy.budgetwise.core.db.dao.TransactionDao
import com.mdrlzy.budgetwise.core.db.entity.AccountEntity
import com.mdrlzy.budgetwise.core.db.entity.CategoryEntity
import com.mdrlzy.budgetwise.core.db.entity.TransactionEntity
import com.mdrlzy.budgetwise.core.db.typeconverter.OffsetDateTimeTypeConverter

@androidx.room.Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        TransactionEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    OffsetDateTimeTypeConverter::class,
)
abstract class BWDatabase: RoomDatabase() {
    abstract fun accountDao(): AccountDao

    abstract fun categoryDao(): CategoryDao

    abstract fun transactionDao(): TransactionDao

    companion object {
        const val DB_NAME = "bw.db"

        fun build(app: Application) =
            Room.databaseBuilder(app, BWDatabase::class.java, DB_NAME).build()
    }
}