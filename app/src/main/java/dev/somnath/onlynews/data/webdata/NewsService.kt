package dev.somnath.onlynews.data.webdata

import dev.somnath.onlynews.model.NewsItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsService{

    @GET("/v2/top-headlines?country=in&apiKey=")
    suspend fun getNews(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=business&apiKey=")
    suspend fun getNewsOnBusiness(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=general&apiKey=")
    suspend fun getNewsOnGeneral(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=health&apiKey=")
    suspend fun getNewsOnHealth(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=technology&apiKey=")
    suspend fun getNewsOnTech(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=science&apiKey=")
    suspend fun getNewsOnScience(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=entertainment&apiKey=")
    suspend fun getNewsOnEntertainment(): Response<NewsItem>
    @GET("/v2/top-headlines?country=in&category=sports&apiKey=")
    suspend fun getNewsOnSports(): Response<NewsItem>

}