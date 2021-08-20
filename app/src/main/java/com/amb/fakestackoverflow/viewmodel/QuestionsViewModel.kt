package com.amb.fakestackoverflow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amb.fakestackoverflow.model.Answer
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.model.repository.StackOverFlowRepository
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
    val loading: MutableLiveData<Boolean> get() = _loading
    val error: MutableLiveData<String> get() = _error

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
            .subscribe({ response ->
                _questionsResponse.value = response.items
                _loading.value = false
                _error.value = null
            }, {
                onError(it.localizedMessage ?: "Error")
            }).also {
                it.dispose()
            }
    }

    fun getAnswers(questionId: Int) {
        repository.getAnswers(questionId)
            .subscribe({ response ->
                _answersResponse.value = response.items
                _loading.value = false
                _error.value = null
            }, {
                onError(it.localizedMessage ?: "Error")
            }).also {
                it.dispose()
            }
    }

    private fun onError(message: String) {
        _error.value = message
        _loading.value = false
    }
}