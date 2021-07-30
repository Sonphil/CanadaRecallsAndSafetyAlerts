package com.sonphil.canadarecallsandsafetyalerts.di

import com.sonphil.canadarecallsandsafetyalerts.data.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.domain.di.qualifier.CanadaApiBaseUrl
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by Sonphil on 28-02-18.
 */
@Module
@InstallIn(SingletonComponent::class)
internal open class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Singleton
    @Provides
    @CanadaApiBaseUrl
    fun provideCanadaApiBaseUrl(): String = "https://healthycanadians.gc.ca"

    @Singleton
    @Provides
    fun provideRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient,
        @CanadaApiBaseUrl baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("$baseUrl/recall-alert-rappel-avis/api/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): CanadaGovernmentApi {
        return retrofit.create(CanadaGovernmentApi::class.java)
    }
}
