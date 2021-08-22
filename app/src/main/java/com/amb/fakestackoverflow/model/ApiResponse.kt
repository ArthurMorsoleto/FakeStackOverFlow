package com.amb.fakestackoverflow.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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
    val creationDate: Long?,

    @SerializedName("tags")
    val tags: ArrayList<String>,

    @SerializedName("link")
    val link: String
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
