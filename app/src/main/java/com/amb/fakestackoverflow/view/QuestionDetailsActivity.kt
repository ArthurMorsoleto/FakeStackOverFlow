package com.amb.fakestackoverflow.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amb.fakestackoverflow.R
import com.amb.fakestackoverflow.di.MyApplication
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.utils.QUESTION_EXTRA
import com.amb.fakestackoverflow.utils.convertDate
import com.amb.fakestackoverflow.utils.convertTitle
import com.amb.fakestackoverflow.viewmodel.QuestionsViewModel
import javax.inject.Inject

class QuestionDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: QuestionsViewModel

    private val answersList by lazy { findViewById<RecyclerView>(R.id.answers_list) }
    private val questionTitle by lazy { findViewById<TextView>(R.id.tvQuestionTitle) }
    private val questionScore by lazy { findViewById<TextView>(R.id.tvQuestionScore) }
    private val questionCreationDate by lazy { findViewById<TextView>(R.id.tvQuestionCreationDate) }
    private val currentTags by lazy { findViewById<TextView>(R.id.tvCurrentTags) }

    private lateinit var answersAdapter: AnswersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_details)

        val question = intent.getParcelableExtra<Question>(QUESTION_EXTRA)

        observerViewModel()

        if (question != null) {
            initView(question)
            question.questionId?.let { viewModel.getAnswers(it) }
        }
    }

    private fun initView(question: Question) {
        answersAdapter = AnswersAdapter(arrayListOf(), this@QuestionDetailsActivity)
        answersList.apply {
            layoutManager = LinearLayoutManager(this@QuestionDetailsActivity)
            adapter = answersAdapter
        }

        questionTitle.text = convertTitle(question.questionTitle)
        questionScore.text = question.score
        questionCreationDate.text = convertDate(question.creationDate)
        currentTags.text = question.tags.toString()
    }

    private fun observerViewModel() {
        viewModel.answersResponse.observe(this, Observer {
            answersAdapter.addItems(it)
        })
    }
}