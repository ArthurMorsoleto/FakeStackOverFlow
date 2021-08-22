package com.amb.fakestackoverflow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amb.fakestackoverflow.model.Answer
import com.amb.fakestackoverflow.model.Owner
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.model.ResponseWrapper
import com.amb.fakestackoverflow.model.repository.StackOverFlowRepository
import com.amb.fakestackoverflow.utils.getOrAwaitValue
import com.amb.fakestackoverflow.viewmodel.QuestionsViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class QuestionsViewModelTest {

    private lateinit var subject: QuestionsViewModel

    private val mockStackOverFlowRepository = mockk<StackOverFlowRepository>(relaxed = true)
    private val fakeQuestion = Question(
        questionId = 1,
        questionTitle = "questionTitle",
        creationDate = 1629473330,
        score = "100",
        tags = arrayListOf(),
        link = ""
    )
    private val fakeAnswer = Answer(
        Owner(displayName = "displayName", profileImage = ""),
        isAccepted = true,
        creationDate = 1629473330
    )

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rxSchedulerRule = RxSchedulerRule()

    @Before
    fun setup() {
        subject = QuestionsViewModel(mockStackOverFlowRepository)
    }

    @Test
    fun `should get next questions page from repository with success`() {
        every {
            mockStackOverFlowRepository.getQuestions(any())
        } returns Single.just(ResponseWrapper(items = listOf(fakeQuestion)))

        subject.getNextPage("android")

        assertEquals(subject.questionsResponse.getOrAwaitValue(), listOf(fakeQuestion))
        assertEquals(subject.page, 1)
        assertEquals(subject.loading.getOrAwaitValue(), false)
        assertEquals(subject.error.getOrAwaitValue(), null)
    }

    @Test
    fun `should show error when get next questions page from repository fails`() {
        every {
            mockStackOverFlowRepository.getQuestions(any())
        } returns Single.error(Throwable("Error"))

        subject.getFirstPage("android")

        assertEquals(subject.page, 1)
        assertEquals(subject.error.getOrAwaitValue(), "Error")
        assertEquals(subject.loading.getOrAwaitValue(), false)
    }

    @Test
    fun `should get answers from repository with success`() {
        every {
            mockStackOverFlowRepository.getAnswers(any())
        } returns Single.just(ResponseWrapper(items = listOf(fakeAnswer)))

        subject.getAnswers(1)

        assertEquals(subject.answersResponse.getOrAwaitValue(), listOf(fakeAnswer))
        assertEquals(subject.loading.getOrAwaitValue(), false)
        assertEquals(subject.error.getOrAwaitValue(), null)
    }

    @Test
    fun `should show error message when get answers from repository fails`() {
        every {
            mockStackOverFlowRepository.getAnswers(any())
        } returns Single.error(Throwable("Error"))

        subject.getAnswers(1)

        assertEquals(subject.loading.getOrAwaitValue(), false)
        assertEquals(subject.error.getOrAwaitValue(), "Error")
    }
}

class RxSchedulerRule : TestRule {

    override fun apply(base: Statement, description: Description) =
        object : Statement() {
            override fun evaluate() {
                RxAndroidPlugins.reset()
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { SCHEDULER_INSTANCE }

                RxJavaPlugins.reset()
                RxJavaPlugins.setIoSchedulerHandler { SCHEDULER_INSTANCE }
                RxJavaPlugins.setNewThreadSchedulerHandler { SCHEDULER_INSTANCE }
                RxJavaPlugins.setComputationSchedulerHandler { SCHEDULER_INSTANCE }

                base.evaluate()
            }
        }

    companion object {
        private val SCHEDULER_INSTANCE = Schedulers.trampoline()
    }
}