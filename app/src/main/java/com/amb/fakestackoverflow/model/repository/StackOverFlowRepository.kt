package com.amb.fakestackoverflow.model.repository

import com.amb.fakestackoverflow.model.Answer
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.model.ResponseWrapper
import com.amb.fakestackoverflow.model.StackOverFlowService
import retrofit2.Call
import javax.inject.Inject

class StackOverFlowRepository @Inject constructor(
    private val stackOverFlowService: StackOverFlowService
) {

    fun getQuestions(page: Int): Call<ResponseWrapper<Question>> {
        return stackOverFlowService.api.getQuestions(page = page)
    }

    fun getAnswers(questionId: Int): Call<ResponseWrapper<Answer>> {
        return stackOverFlowService.api.getAnswers(questionId)
    }
}