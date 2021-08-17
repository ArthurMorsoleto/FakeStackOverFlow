package com.amb.fakestackoverflow.view

import com.amb.fakestackoverflow.model.Question

interface OnQuestionClick {
    fun onClick(question: Question)
}