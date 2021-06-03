package com.algebra.wheathertask.di

import android.content.Context
import com.algebra.wheathertask.constants.Constants
import com.algebra.wheathertask.networking.SearchingFilterNetworkService
import com.algebra.wheathertask.networking.SocialNetworkService
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiServiceModule {

    @Provides
    @Singleton
    fun provideApiService(@SocialNetworkRetrofit retrofit: Retrofit): SocialNetworkService{
        return retrofit.create(SocialNetworkService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiServiceSearching(@SearchingFilterNetworkRetrofit retrofit: Retrofit): SearchingFilterNetworkService{
        return retrofit.create(SearchingFilterNetworkService::class.java)
    }

    @SocialNetworkRetrofit
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

    @SearchingFilterNetworkRetrofit
    @Provides
    @Singleton
    fun provideRetrofitSearching(client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_SEARCHING)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(ChuckInterceptor(context))
            .addInterceptor(logging)
            .build()
    }
}