package com.amb.fakestackoverflow.model

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackOverFlowApi {

    @GET(value = "questions?&order=desc&sort=votes&tagged=android&site=stackoverflow")
    fun getQuestions(
        @Query("page")
        page: Int
    ): Single<ResponseWrapper<Question>>

    @GET(value = "questions/{id}/answers?&order=desc&sort=votes&site=stackoverflow")
    fun getAnswers(@Path("id") id: Int): Single<ResponseWrapper<Answer>>
}