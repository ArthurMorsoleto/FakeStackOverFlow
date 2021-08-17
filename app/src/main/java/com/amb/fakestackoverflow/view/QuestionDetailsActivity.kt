package com.amb.fakestackoverflow.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amb.fakestackoverflow.QUESTION_EXTRA
import com.amb.fakestackoverflow.R
import com.amb.fakestackoverflow.model.Question

class QuestionDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_details)

        val question = intent.getParcelableExtra<Question>(QUESTION_EXTRA)
        Toast.makeText(applicationContext, question?.questionId.toString(), Toast.LENGTH_SHORT)
            .show()
    }
}