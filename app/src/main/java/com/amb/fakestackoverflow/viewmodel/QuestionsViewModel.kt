package com.amb.fakestackoverflow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amb.fakestackoverflow.model.Answer
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.model.ResponseWrapper
import com.amb.fakestackoverflow.model.repository.StackOverFlowRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class QuestionsViewModel @Inject constructor(
    private val repository: StackOverFlowRepository
) : ViewModel() {

    private val _questionsResponse = MutableLiveData<List<Question>>()
    private val _answersResponse = MutableLiveData<List<Answer>>()
    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String>()

    val questionsResponse: MutableLiveData<List<Question>> get() = _questionsResponse
    val answersResponse: MutableLiveData<List<Answer>> get() = _answersResponse
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

    private fun getQuestions() {
        repository.getQuestions(page)
            .enqueue(object : Callback<ResponseWrapper<Question>> {

                override fun onResponse(
                    call: Call<ResponseWrapper<Question>>,
                    response: Response<ResponseWrapper<Question>>
                ) {
                    if (response.isSuccessful) {
                        val questionList = response.body()

                        questionList?.let {
                            _questionsResponse.value = questionList.items
                            _loading.value = false
                            _error.value = null
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<Question>>, t: Throwable) {
                    onError(t.localizedMessage ?: "Error")
                }
            })
    }

    fun getAnswers(questionId: Int) {
        repository.getAnswers(questionId)
            .enqueue(object : Callback<ResponseWrapper<Answer>> {
                override fun onResponse(
                    call: Call<ResponseWrapper<Answer>>,
                    response: Response<ResponseWrapper<Answer>>
                ) {
                    if (response.isSuccessful) {
                        val answersList = response.body()

                        answersList?.let {
                            _answersResponse.value = answersList.items
                            _loading.value = false
                            _error.value = null
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<Answer>>, t: Throwable) {
                    onError(t.localizedMessage ?: "Error")
                }
            })
    }

    private fun onError(message: String) {
        _error.value = message
        _loading.value = false
    }
}