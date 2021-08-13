package com.amb.fakestackoverflow.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amb.fakestackoverflow.R
import com.amb.fakestackoverflow.viewmodel.QuestionsViewModel

class MainActivity : AppCompatActivity() {

    private val questionsList by lazy { findViewById<RecyclerView>(R.id.questions_list) }
    private val listErrorText by lazy { findViewById<TextView>(R.id.list_error) }
    private val loadingView by lazy { findViewById<ProgressBar>(R.id.loading_view) }

    private val questionsAdapter = QuestionsAdapter(arrayListOf())
    private val lm = LinearLayoutManager(this)
    private val viewModel: QuestionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        questionsList.apply {
            layoutManager = lm
            adapter = questionsAdapter
        }

        observeViewModel()

        viewModel.getQuestions()
    }


    private fun observeViewModel() {
        viewModel.questionsResponse.observe(this, Observer { items ->
            items?.let {
                questionsList.visibility = View.VISIBLE
                questionsAdapter.addQuestions(it)
            }
        })

        viewModel.error.observe(this, Observer { errorMsg ->
            listErrorText.visibility = if (errorMsg == null) View.GONE else View.VISIBLE
            listErrorText.text = "Error\n$errorMsg"
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listErrorText.visibility = View.GONE
                    questionsList.visibility = View.GONE
                }
            }
        })
    }
}
