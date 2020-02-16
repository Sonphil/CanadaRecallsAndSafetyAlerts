package com.sonphil.canadarecallsandsafetyalerts.di

import androidx.lifecycle.ViewModel
import com.sonphil.canadarecallsandsafetyalerts.di.scope.RecallDetailsScope
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsActivity
import com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details.RecallDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Created by Sonphil on 11-02-20.
 */

@Module(includes = [RecallDetailsActivityModule.RecallDetailsActivityArgsModule::class])
interface RecallDetailsActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecallDetailsViewModel::class)
    @RecallDetailsScope
    fun bindRecallDetailsViewModel(recallDetailsViewModel: RecallDetailsViewModel): ViewModel

    @Module
    open class RecallDetailsActivityArgsModule {
        @Provides
        @RecallDetailsScope
        fun provideRecall(recallDetailsActivity: RecallDetailsActivity): Recall {
            return recallDetailsActivity.getRecall()!!
        }
    }
}