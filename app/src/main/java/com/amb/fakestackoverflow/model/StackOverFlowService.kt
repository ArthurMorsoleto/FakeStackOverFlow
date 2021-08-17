package com.amb.fakestackoverflow.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object StackOverFlowService {
    private const val STACK_OVERFLOW_API_URL = "https://api.stackexchange.com/2.2/"

    val api = Retrofit.Builder()
        .baseUrl(STACK_OVERFLOW_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create<StackOverFlowApi>()
}
