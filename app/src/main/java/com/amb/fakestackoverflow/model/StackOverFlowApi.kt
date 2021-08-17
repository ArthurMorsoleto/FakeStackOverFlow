package com.amb.fakestackoverflow.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StackOverFlowApi {

    @GET(value = "questions?&order=desc&sort=votes&tagged=android&site=stackoverflow")
    fun getQuestions(
        @Query("page")
        page: Int
    ): Call<ResponseWrapper<Question>>
}