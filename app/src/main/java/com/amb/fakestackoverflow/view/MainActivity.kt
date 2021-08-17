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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amb.fakestackoverflow.R
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.viewmodel.QuestionsViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: QuestionsViewModel by viewModels()

    private val questionsList by lazy { findViewById<RecyclerView>(R.id.questions_list) }
    private val listErrorText by lazy { findViewById<TextView>(R.id.list_error) }
    private val loadingView by lazy { findViewById<ProgressBar>(R.id.loading_view) }
    private val swipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.swipe_layout) }

    private lateinit var questionsAdapter: QuestionsAdapter
    private lateinit var lm: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeViewModel()
        initViews()
        viewModel.getNextPage()
    }

    private fun initViews() {
        setupQuestionList()
        setupRefreshLayout()
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
            viewModel.getFirstPage()
            loadingView.visibility = View.VISIBLE
            questionsList.visibility = View.GONE
        }
    }

    private fun setupQuestionList() {
        val questionClickListener = object : OnQuestionClick {
            override fun onClick(question: Question) {

            }
        }

        questionsAdapter = QuestionsAdapter(arrayListOf(), questionClickListener)
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
                            viewModel.getNextPage()
                        }
                    }
                }
            })
        }
    }
}
