package com.mujapps.jetpackcapstone.di

import com.mujapps.jetpackcapstone.network.ReaderApiService
import com.mujapps.jetpackcapstone.repository.ReaderBooksRepository
import com.mujapps.jetpackcapstone.utils.ReaderConstants

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoginInterceptor(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(httpLoggingInterceptor)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideBooksApi(builder: OkHttpClient): ReaderApiService {
        return Retrofit.Builder().baseUrl(ReaderConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(builder).build()
            .create(ReaderApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideBookRepository(mReaderApiService: ReaderApiService) =
        ReaderBooksRepository(mReaderApiService)
}