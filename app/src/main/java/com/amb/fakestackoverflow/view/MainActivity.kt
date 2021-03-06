package com.amb.fakestackoverflow.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amb.fakestackoverflow.R
import com.amb.fakestackoverflow.di.MyApplication
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.utils.QUESTION_EXTRA
import com.amb.fakestackoverflow.viewmodel.QuestionsViewModel
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: QuestionsViewModel

    private val questionsList by lazy { findViewById<RecyclerView>(R.id.questions_list) }
    private val listErrorText by lazy { findViewById<TextView>(R.id.list_error) }
    private val loadingView by lazy { findViewById<ProgressBar>(R.id.loading_view) }
    private val swipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.swipe_layout) }
    private val textInputSearch by lazy { findViewById<TextInputLayout>(R.id.textInputSearch) }
    private val buttonSearch by lazy { findViewById<Button>(R.id.buttonSearch) }

    private lateinit var questionsAdapter: QuestionsAdapter
    private lateinit var lm: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeViewModel()
        initViews()
        viewModel.getNextPage(textInputSearch.editText?.text.toString())
    }

    private fun initViews() {
        setupQuestionList()
        setupRefreshLayout()
        setupSearchButton()
    }

    private fun setupSearchButton() {
        buttonSearch.setOnClickListener {
            val tag = textInputSearch.editText?.text.toString()
            viewModel.getFirstPage(tag)
            questionsAdapter.clearQuestions()
        }
    }

    private fun observeViewModel() {
        viewModel.questionsResponse.observe(this, Observer { items ->
            items?.let {
                questionsList.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false
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

    private fun setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            questionsAdapter.clearQuestions()

            textInputSearch.editText?.setText("")
            textInputSearch.clearFocus()

            viewModel.getFirstPage(textInputSearch.editText?.text.toString())

            loadingView.visibility = View.VISIBLE
            questionsList.visibility = View.GONE
        }
    }

    private fun setupQuestionList() {
        val questionClickListener = object : OnQuestionClick {
            override fun onClick(question: Question) {
                loadQuestionDetailScreen(question)
            }
        }

        questionsAdapter = QuestionsAdapter(arrayListOf(), questionClickListener, this@MainActivity)
        lm = LinearLayoutManager(this)

        questionsList.apply {
            layoutManager = lm
            adapter = questionsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val childCount = questionsAdapter.itemCount
                        val lastPosition = lm.findLastVisibleItemPosition()
                        if (childCount - 1 == lastPosition && loadingView.visibility == View.GONE) {
                            loadingView.visibility = View.VISIBLE
                            viewModel.getNextPage(textInputSearch.editText?.text.toString())
                        }
                    }
                }
            })
        }
    }

    private fun loadQuestionDetailScreen(question: Question) {
        startActivity(Intent(
            this@MainActivity,
            QuestionDetailsActivity::class.java
        ).apply {
            putExtra(QUESTION_EXTRA, question)
        })
    }
}
