package dev.somnath.onlynews.ui.main

import android.util.EventLog
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import dev.somnath.onlynews.R
import dev.somnath.onlynews.data.prefrences.AppPrefrences
import dev.somnath.onlynews.data.localdata.Bookmarks
import dev.somnath.onlynews.data.localdata.BookmarksRepository
import dev.somnath.onlynews.model.NewsItem
import dev.somnath.onlynews.data.repo.ArticleProvider
import dev.somnath.onlynews.model.Article
import dev.somnath.onlynews.utils.Event
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel(
    private val repository: BookmarksRepository,
    private val prefrences: AppPrefrences
) : ViewModel(), Observable {
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    @Bindable
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    val category: MutableLiveData<String> = MutableLiveData("Top Headlines")
    private val message : MutableLiveData<Event<String>> = MutableLiveData()

    val errorMessage
    get() = message

    var responseLiveData: MutableLiveData<Response<NewsItem>> = MutableLiveData()

    var bookmarkList: LiveData<List<Bookmarks>> = repository.bookmarks

    val appPrefrences: AppPrefrences
    get() = prefrences




    fun refreshResponse() {

        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = ArticleProvider().getNews(category.value!!)
                if (response.isSuccessful) {
                    responseLiveData.postValue(response)
                }
                else {
                    responseLiveData.postValue(null)
                    message.value = Event(response.errorBody().toString())
                }
            }catch (e: Exception){
                responseLiveData.postValue(null)
                message.value = Event(e.toString())
            }

            isLoading.value = false
        }

    }

    fun changeViewType(id: Int) {

        when (id) {
            R.id.radio_button1 -> {
                viewModelScope.launch {
                    prefrences.saveViewType("List")
                }
            }
            R.id.radio_button2 -> {
                viewModelScope.launch {
                    prefrences.saveViewType("Tab")
                }
            }
        }

        refreshResponse()
    }

    fun addABookmark(id: Int = 0, article: Article) {

        viewModelScope.launch {
            val bookMark = Bookmarks(id, article)
            repository.insertArticleIntoBookmarks(bookMark)
        }

    }

    fun deleteABookmark(bookmark: Bookmarks) {
        viewModelScope.launch {
            repository.deleteArticlefromBookmarks(bookmark)
        }
    }

    fun clearAllBookmarks() {
        viewModelScope.launch {
            repository.deleteALlArticleFromBookmarks()
        }
    }

}