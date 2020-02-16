package com.sonphil.canadarecallsandsafetyalerts.di

import com.sonphil.canadarecallsandsafetyalerts.api.CanadaGovernmentApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by Sonphil on 28-02-18.
 */
@Module
internal open class NetworkModule {

    @Singleton @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Singleton @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Singleton @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://healthycanadians.gc.ca/recall-alert-rappel-avis/api/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient)
                .build()
    }

    @Singleton @Provides
    fun provideApi(retrofit: Retrofit): CanadaGovernmentApi {
        return retrofit.create(CanadaGovernmentApi::class.java)
    }
}
