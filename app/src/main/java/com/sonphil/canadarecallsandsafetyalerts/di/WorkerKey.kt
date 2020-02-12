package com.sonphil.canadarecallsandsafetyalerts.di

import androidx.work.ListenableWorker
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Created by Sonphil on 12-02-20.
 */

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)