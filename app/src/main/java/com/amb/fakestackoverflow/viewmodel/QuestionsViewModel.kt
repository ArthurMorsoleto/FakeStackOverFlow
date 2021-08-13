package com.amb.fakestackoverflow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amb.fakestackoverflow.model.DummyDataProvider
import com.amb.fakestackoverflow.model.Question

class QuestionsViewModel : ViewModel() {

    val questionsResponse = MutableLiveData<List<Question>>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun getQuestions() {
        questionsResponse.value = DummyDataProvider.getDummyData(30)
        loading.value = false
        error.value = null
    }

    private fun onError(message: String) {
        error.value = message
        loading.value = false
    }
}