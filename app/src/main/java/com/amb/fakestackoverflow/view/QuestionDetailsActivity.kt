package com.amb.fakestackoverflow.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.amb.fakestackoverflow.QUESTION_EXTRA
import com.amb.fakestackoverflow.R
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.viewmodel.QuestionsViewModel

class QuestionDetailsActivity : AppCompatActivity() {

    private val viewModel: QuestionsViewModel by viewModels()

    private val answersList by lazy { findViewById<RecyclerView>(R.id.answers_list) }
    //TODO(add list item layout)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_details)

        val question = intent.getParcelableExtra<Question>(QUESTION_EXTRA)

        observerViewModel()

        if (question != null) {
            initView(question)
            viewModel.getAnswers(question.questionId)
        }
    }

    private fun initView(question: Question) {
        TODO("add chassi layout to question details")
    }

    private fun observerViewModel() {
        viewModel.answersResponse.observe(this, Observer {
            TODO("add list items")
        })
    }
}