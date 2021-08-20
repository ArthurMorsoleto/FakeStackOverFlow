package com.amb.fakestackoverflow.model.repository

import com.amb.fakestackoverflow.model.Answer
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.model.ResponseWrapper
import com.amb.fakestackoverflow.model.StackOverFlowService
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class StackOverFlowRepository @Inject constructor(
    private val stackOverFlowService: StackOverFlowService
) {

    fun getQuestions(page: Int): Single<ResponseWrapper<Question>> {
        return stackOverFlowService.api.getQuestions(page = page)
    }

    fun getAnswers(questionId: Int): Single<ResponseWrapper<Answer>> {
        return stackOverFlowService.api.getAnswers(questionId)
    }
}