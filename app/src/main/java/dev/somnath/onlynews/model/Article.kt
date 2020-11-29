package dev.somnath.onlynews.model

import androidx.room.Embedded
import java.io.Serializable

data class Article(
    val author: String?,
    var content: String?,
    val description: String?,
    val publishedAt: String?,
    @Embedded val source: Source,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Serializable