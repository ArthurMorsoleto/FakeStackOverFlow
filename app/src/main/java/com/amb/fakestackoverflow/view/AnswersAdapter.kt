package com.amb.fakestackoverflow.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amb.fakestackoverflow.R
import com.amb.fakestackoverflow.model.Answer
import com.amb.fakestackoverflow.model.convertDate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.answers_layout.view.*

class AnswersAdapter(
    private val answers: ArrayList<Answer>,
    private val context: Context
) : RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.answers_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bind(answers[position], context)
    }

    fun addItems(newAnswers: List<Answer>) {
        val currentLength = answers.size
        answers.addAll(newAnswers)
        notifyItemInserted(currentLength)
    }

    class AnswerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val answerOwner = view.tvAnswerOwner
        private val answerCreationDate = view.tvAnswerCreationDate
        private val answerAccepted = view.tvAnswerAccepted
        private val ownerAvatar = view.ivOwner

        fun bind(answer: Answer, context: Context) {
            answerOwner.text = answer.owner.displayName
            answerCreationDate.text = convertDate(answer.creationDate)
            answerAccepted.run {
                if (answer.isAccepted) {
                    text = "Accepted"
                    setTextColor(resources.getColor(R.color.colorAccepted))
                } else {
                    text = "Not Accepted"
                    setTextColor(resources.getColor(R.color.colorDenied))
                }
            }

            Picasso.with(context)
                .load(answer.owner.profileImage)
                .into(ownerAvatar)
        }
    }
}