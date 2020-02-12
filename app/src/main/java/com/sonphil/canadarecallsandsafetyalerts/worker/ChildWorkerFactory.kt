package com.sonphil.canadarecallsandsafetyalerts.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

/**
 * Created by Sonphil on 12-02-20.
 */

interface ChildWorkerFactory {
    fun create(appContext: Context, params: WorkerParameters): ListenableWorker
}