package com.mdrlzy.budgetwise.feature.transactions.impl.domain.usecase

import com.mdrlzy.budgetwise.feature.transactions.impl.domain.model.Transaction

class ResolveTransactionSyncConflictUseCase {
    operator fun invoke(local: Transaction, remote: Transaction): Transaction {
        return if (local.updatedAt >= remote.updatedAt) local else remote
    }
}