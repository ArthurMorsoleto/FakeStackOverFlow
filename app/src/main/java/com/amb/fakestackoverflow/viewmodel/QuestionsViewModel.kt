package com.amb.fakestackoverflow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amb.fakestackoverflow.model.Answer
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.model.ResponseWrapper
import com.amb.fakestackoverflow.model.StackOverFlowService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionsViewModel : ViewModel() {

    val questionsResponse = MutableLiveData<List<Question>>()
    val answersResponse = MutableLiveData<List<Answer>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    var page = 0

    fun getNextPage() {
        page++
        getQuestions()
    }

    fun getFirstPage() {
        page = 1
        getQuestions()
    }

    fun getAnswers(questionId: Int?) {
        if (questionId != null) {
            StackOverFlowService.api.getAnswers(questionId)
                .enqueue(object : Callback<ResponseWrapper<Answer>> {
                    override fun onResponse(
                        call: Call<ResponseWrapper<Answer>>,
                        response: Response<ResponseWrapper<Answer>>
                    ) {
                        if (response.isSuccessful) {
                            val answersList = response.body()

                            answersList?.let {
                                answersResponse.value = answersList.items
                                loading.value = false
                                error.value = null
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseWrapper<Answer>>, t: Throwable) {
                        onError(t.localizedMessage ?: "Error")
                    }
                })
        } else {
            onError("Question Id must not be null")
        }
    }

    private fun getQuestions() {
        StackOverFlowService.api.getQuestions(page = page)
            .enqueue(object : Callback<ResponseWrapper<Question>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<Question>>,
                    response: Response<ResponseWrapper<Question>>
                ) {
                    if (response.isSuccessful) {
                        val questionList = response.body()

                        questionList?.let {
                            questionsResponse.value = questionList.items
                            loading.value = false
                            error.value = null
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<Question>>, t: Throwable) {
                    onError(t.localizedMessage ?: "Error")
                }
            })
    }

    private fun onError(message: String) {
        error.value = message
        loading.value = false
    }
}