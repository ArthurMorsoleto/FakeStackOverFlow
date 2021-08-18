package com.amb.fakestackoverflow.model

import android.os.Build
import android.os.Parcelable
import android.text.Html
import android.text.format.DateFormat
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

data class ResponseWrapper<T>(
    val items: List<T>
)

@Parcelize
data class Question(
    @SerializedName("question_id")
    val questionId: Int?,

    @SerializedName("title")
    val questionTitle: String?,

    @SerializedName("score")
    val score: String?,

    @SerializedName("creation_date")
    val creationDate: Long?
) : Parcelable

data class Answer(
    @SerializedName("owner")
    val owner: Owner,

    @SerializedName("is_accepted")
    val isAccepted: Boolean,

    @SerializedName("creation_date")
    val creationDate: Long?
)

data class Owner(
    @SerializedName("display_name")
    val displayName: String,

    @SerializedName("profile_image")
    val profileImage: String?
)

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
