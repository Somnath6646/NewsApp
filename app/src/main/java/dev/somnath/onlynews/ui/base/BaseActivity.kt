package dev.somnath.onlynews.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import dev.somnath.onlynews.data.prefrences.AppPrefrences
import dev.somnath.onlynews.data.localdata.BookmarksDatabase
import dev.somnath.onlynews.data.localdata.BookmarksRepository
import dev.somnath.onlynews.ui.main.MainViewModelFactory

abstract class BaseActivity<binding : ViewDataBinding, viewModel : ViewModel, viewModelstoreOwner : ViewModelStoreOwner>() :
    AppCompatActivity() {

    abstract fun getViewBinding(): binding
    abstract fun getViewModel(): Class<viewModel>
    abstract fun getViewModelStoreOwner(): viewModelstoreOwner
    abstract fun getContext(): Context

    protected lateinit var binding: binding

    protected lateinit var viewModel: viewModel

    protected lateinit var prefrences: AppPrefrences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        val database: BookmarksDatabase =
            BookmarksDatabase.getInstance(context = applicationContext)
        val dao = database.dao
        val repository = BookmarksRepository(dao)

        prefrences = AppPrefrences(this)

        val factory = MainViewModelFactory(repository, prefrences)
        viewModel = ViewModelProvider(getViewModelStoreOwner(), factory).get(getViewModel())
    }
}