package dev.somnath.onlynews.model

import dev.somnath.onlynews.model.Article

data class NewsItem(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)