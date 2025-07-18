class SyncWorker(
    params: WorkerParameters,
    private val context: Context,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val core = (context.applicationContext as CoreComponentProvider).provideCoreComponent()
        core.pendingTransactionDao()


        return Result.success()
    }

    companion object {
        const val NAME = "SyncWorker"
    }
}