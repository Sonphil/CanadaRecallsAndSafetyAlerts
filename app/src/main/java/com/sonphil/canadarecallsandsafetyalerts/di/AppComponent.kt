package com.sonphil.canadarecallsandsafetyalerts.di

import android.app.Application
import com.sonphil.canadarecallsandsafetyalerts.presentation.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Sonphil on 28-02-18.
 */
@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ViewModelFactoryModule::class,
    MainActivityBuilder::class,
    DatabaseModule::class,
    NetworkModule::class
])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): Builder
        fun build(): AppComponent
    }

    override fun inject(app: App)
}