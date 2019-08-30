package com.wmapp.dipinjections.modules

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.wmapp.BuildConfig
import com.wmapp.common.AppConstants
import com.wmapp.dipinjections.customscope.ApplicationScope
import com.wmapp.networking.NetworkProcessor
import com.wmapp.networking.NetworkService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Responsible for creating retrofit dependency.
 * Can have more internal dependency such as authentication as well.
 */
@Module
class NetworkModule{

    @Provides
    @ApplicationScope
    internal fun provideCall(): Retrofit {

        var logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(AppConstants.TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(AppConstants.TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .addConverterFactory(providesGsonConverterFactory())
            .baseUrl(BuildConfig.BASE_URL).build()
    }

    @Provides
    @ApplicationScope
    fun providesNetworkService(retrofit: Retrofit): NetworkService {
        return retrofit.create(NetworkService::class.java)
    }

    @Provides
    @ApplicationScope
    fun providesService(networkService: NetworkService): NetworkProcessor {
        return NetworkProcessor(networkService)
    }

    private fun providesGsonConverterFactory(): GsonConverterFactory {
        val gsonBuilder = GsonBuilder()
        return GsonConverterFactory.create(gsonBuilder.create())
    }
}