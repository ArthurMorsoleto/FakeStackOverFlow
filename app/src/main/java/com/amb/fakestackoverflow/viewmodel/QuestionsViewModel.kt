package com.amb.fakestackoverflow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amb.fakestackoverflow.model.Answer
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.model.repository.StackOverFlowRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class QuestionsViewModel @Inject constructor(
    private val repository: StackOverFlowRepository
) : ViewModel() {

    private val _questionsResponse = MutableLiveData<List<Question>>()
    private val _answersResponse = MutableLiveData<List<Answer>>()
    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()

    val questionsResponse: MutableLiveData<List<Question>> get() = _questionsResponse
    val answersResponse: MutableLiveData<List<Answer>> get() = _answersResponse
    val loading: MutableLiveData<Boolean> get() = _loading
    val error: MutableLiveData<String> get() = _error

    var page = 0

    fun getNextPage(currentTag: String) {
        page++
        getQuestions(currentTag)
    }

    fun getFirstPage(currentTag: String) {
        page = 1
        getQuestions(currentTag)
    }

    private fun getQuestions(currentTag: String) {
        repository.getQuestions(page, currentTag)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                _questionsResponse.postValue(response.items)
                _loading.value = false
                _error.value = null
            }, {
                onError(it.localizedMessage ?: "Error")
            }).also {
                compositeDisposable.add(it)
            }
    }

    fun getAnswers(questionId: Int) {
        repository.getAnswers(questionId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                _answersResponse.value = response.items
                _loading.value = false
                _error.value = null
            }, {
                onError(it.localizedMessage ?: "Error")
            }).also {
                compositeDisposable.add(it)
            }
    }

    private fun onError(message: String) {
        _error.value = message
        _loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}