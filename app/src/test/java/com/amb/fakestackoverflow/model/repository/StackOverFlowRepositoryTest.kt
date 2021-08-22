package com.amb.fakestackoverflow.model.repository

import com.amb.fakestackoverflow.model.Answer
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.model.ResponseWrapper
import com.amb.fakestackoverflow.model.StackOverFlowService
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class StackOverFlowRepositoryTest {

    private lateinit var subject: StackOverFlowRepository
    private val mockStackOverFlowService = mockk<StackOverFlowService>()
    private val fakeQuestionResponse = Single.just(ResponseWrapper<Question>(items = listOf()))
    private val fakeAnswerResponse = Single.just(ResponseWrapper<Answer>(items = listOf()))

    @Before
    fun setup() {
        subject = StackOverFlowRepository(mockStackOverFlowService)
    }

    @Test
    fun `should call get question from service with success`() {
        every {
            mockStackOverFlowService.api.getQuestions(any(), any())
        } returns fakeQuestionResponse

        val result = subject.getQuestions(1)

        Assert.assertEquals(result, fakeQuestionResponse)
    }

    @Test
    fun `should call get answers from service with success`() {
        every {
            mockStackOverFlowService.api.getAnswers(any())
        } returns fakeAnswerResponse

        val result = subject.getAnswers(1)

        Assert.assertEquals(result, fakeAnswerResponse)
    }
}