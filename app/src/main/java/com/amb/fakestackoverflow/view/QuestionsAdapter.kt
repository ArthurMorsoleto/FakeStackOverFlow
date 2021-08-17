package com.amb.fakestackoverflow.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amb.fakestackoverflow.R
import com.amb.fakestackoverflow.model.Question
import com.amb.fakestackoverflow.model.convertDate
import com.amb.fakestackoverflow.model.convertTitle
import kotlinx.android.synthetic.main.question_layout.view.*

class QuestionsAdapter(
    private val questions: ArrayList<Question>,
    private val listener: OnQuestionClick
) : RecyclerView.Adapter<QuestionsAdapter.AdapterViewHolder>() {

    fun addQuestions(newQuestions: List<Question>) {
        val currentLength = questions.size
        questions.addAll(newQuestions)
        notifyItemInserted(currentLength)
    }

    fun clearQuestions() {
        questions.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AdapterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.question_layout, parent, false)
        )

    override fun getItemCount() = questions.size

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.bind(questions[position], listener)
    }

    class AdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val questionView = view
        private val title: TextView = view.item_title
        private val score: TextView = view.item_score
        private val date: TextView = view.item_date

        fun bind(
            question: Question,
            listener: OnQuestionClick
        ) {
            title.text = convertTitle(question.questionTitle)
            score.text = question.score
            date.text = convertDate(question.creationDate)

            questionView.setOnClickListener {
                listener.onClick(question)
            }
        }
    }
}
