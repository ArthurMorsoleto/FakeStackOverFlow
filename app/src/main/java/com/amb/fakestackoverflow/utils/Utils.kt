package com.amb.fakestackoverflow.utils

import android.os.Build
import android.text.Html
import android.text.format.DateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

const val QUESTION_EXTRA = "QUESTION_EXTRA"

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

fun convertTitle(title: String?): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(title).toString()
    }
}

fun convertDate(timeStamp: Long?): String {
    timeStamp?.let {
        val calender = Calendar.getInstance()
        calender.timeInMillis = timeStamp * 1000
        return DateFormat.format("dd/MM/yyyy hh:mm", calender).toString()
    }
    return ""
}
