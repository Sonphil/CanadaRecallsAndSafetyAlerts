package com.sonphil.canadarecallsandsafetyalerts.di

import androidx.lifecycle.ViewModel
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Sonphil on 11-02-20.
 */

@Module
interface RecallDetailsActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecallDetailsViewModel::class)
    fun bindRecallDetailsViewModel(recallDetailsViewModel: RecallDetailsViewModel): ViewModel
}