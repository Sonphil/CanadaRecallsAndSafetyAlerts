package com.sonphil.canadarecallsandsafetyalerts.di

import android.app.Application
import com.sonphil.canadarecallsandsafetyalerts.presentation.App
import com.sonphil.canadarecallsandsafetyalerts.worker.AppWorkerFactory
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Sonphil on 28-02-18.
 */
@Singleton
@Component(
    modules = [
        ActivityBindingModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        DatabaseModule::class,
        NetworkModule::class,
        ReceiverBindingModule::class,
        RepositoryModule::class,
        ViewModelFactoryModule::class,
        WorkerFactoryModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(app: App)

    fun workerFactory(): AppWorkerFactory
}
