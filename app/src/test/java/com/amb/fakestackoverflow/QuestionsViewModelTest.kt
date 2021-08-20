package com.amb.fakestackoverflow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.model.ResponseWrapper
import com.amb.fakestackoverflow.model.repository.StackOverFlowRepository
import com.amb.fakestackoverflow.utils.getOrAwaitValue
import com.amb.fakestackoverflow.viewmodel.QuestionsViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.*
import org.mockito.ArgumentMatchers.anyInt
import retrofit2.mock.Calls

class QuestionsViewModelTest {

    private lateinit var robot: QuestionsViewModel

    private val mockStackOverFlowRepository = mockk<StackOverFlowRepository>(relaxed = true)
    private val fakeQuestion = Question(
        questionId = 1,
        questionTitle = "questionTitle",
        creationDate = 1629473330,
        score = "100"
    )

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        robot = QuestionsViewModel(mockStackOverFlowRepository)
    }

    @Test
    @Ignore
    fun `should get next questions page from repository with success`() {
        every {
            mockStackOverFlowRepository.getQuestions(anyInt())
        } returns Calls.response(ResponseWrapper(items = listOf(fakeQuestion)))

        robot.getNextPage()

        Assert.assertEquals(robot.questionsResponse.getOrAwaitValue(), listOf(fakeQuestion))
        Assert.assertEquals(robot.page, 1)
    }
}