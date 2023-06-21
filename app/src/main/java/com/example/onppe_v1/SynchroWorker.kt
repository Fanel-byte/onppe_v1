package com.example.onppe_v1

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class SynchroWorker (val context: Context, val params: WorkerParameters): CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {
        val signalements = AppDatabase.buildDatabase(context)?.getSignalementDao()
            ?.getSignalementToSynchronize()


        val response = RetrofitService.endpoint.insertSignalements(signalements!!)

        if(response.isSuccessful) {
            val newSignalements = signalements?.map { it.copy(upload = 1) }
            AppDatabase.buildDatabase(context)?.getSignalementDao()?.updateSignalements(newSignalements)
            return  Result.success()
        }
        else {
            return Result.retry()
        }
    }
}