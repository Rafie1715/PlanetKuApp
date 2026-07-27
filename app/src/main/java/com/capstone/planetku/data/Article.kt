package com.capstone.planetku.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val title: String = "",
    val description: String = "",
    val content: String = "",
    val url: String = "",
    val urlToImage: String = "",
    val publishedAt: String = ""
) : Parcelable
