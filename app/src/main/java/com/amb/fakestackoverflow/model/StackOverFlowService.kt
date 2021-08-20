package com.amb.fakestackoverflow.model

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject

class StackOverFlowService @Inject constructor() {

    private fun loggingInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }

        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    val api = Retrofit.Builder()
        .baseUrl(STACK_OVERFLOW_API_URL)
        .client(loggingInterceptor())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create<StackOverFlowApi>()

    companion object {
        private const val STACK_OVERFLOW_API_URL = "https://api.stackexchange.com/2.2/"
    }
}
