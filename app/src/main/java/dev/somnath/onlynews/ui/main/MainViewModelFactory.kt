package dev.somnath.onlynews.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.somnath.onlynews.data.prefrences.AppPrefrences
import dev.somnath.onlynews.data.localdata.BookmarksRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val repository: BookmarksRepository, private val preferences: AppPrefrences): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository,preferences) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}