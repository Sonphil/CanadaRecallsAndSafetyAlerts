package com.sonphil.canadarecallsandsafetyalerts.presentation

import com.sonphil.canadarecallsandsafetyalerts.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

/**
 * Created by Sonphil on 01-02-20.
 */

class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}