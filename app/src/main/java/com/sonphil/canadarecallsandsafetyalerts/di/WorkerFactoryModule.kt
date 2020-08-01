package com.sonphil.canadarecallsandsafetyalerts.di

import com.sonphil.canadarecallsandsafetyalerts.worker.ChildWorkerFactory
import com.sonphil.canadarecallsandsafetyalerts.worker.SyncRecallsWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Sonphil on 12-02-20.
 */

@Module
interface WorkerFactoryModule {
    @Binds
    @IntoMap
    @WorkerKey(SyncRecallsWorker::class)
    fun bindSyncRecallsWorker(factory: SyncRecallsWorker.Factory): ChildWorkerFactory
}
