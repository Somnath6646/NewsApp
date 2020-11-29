package dev.somnath.onlynews.ui.articledetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import dev.somnath.onlynews.R
import dev.somnath.onlynews.data.localdata.BookmarksDatabase
import dev.somnath.onlynews.data.localdata.BookmarksRepository
import dev.somnath.onlynews.data.prefrences.AppPrefrences
import dev.somnath.onlynews.databinding.FragmentArticlesBinding
import dev.somnath.onlynews.model.Article
import dev.somnath.onlynews.ui.adapter.NewsListAdapter
import dev.somnath.onlynews.ui.adapter.NewsTabAdapter
import dev.somnath.onlynews.ui.main.MainActivity
import dev.somnath.onlynews.ui.main.MainViewModel
import dev.somnath.onlynews.ui.main.MainViewModelFactory
import dev.somnath.onlynews.utils.HidingViewScrollListener
import dev.somnath.onlynews.utils.ObjectSerializer


class ArticlesFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    lateinit var binding: FragmentArticlesBinding
    private lateinit var newsListAdapter: NewsListAdapter
    private lateinit var newsTabAdapter: NewsTabAdapter
    private lateinit var preferences: AppPrefrences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_articles, container, false)

        val database: BookmarksDatabase = BookmarksDatabase.getInstance(context = requireContext())
        val dao = database.dao
        val repository = BookmarksRepository(dao)

        preferences =
            AppPrefrences(requireContext())

        val factory = MainViewModelFactory(repository, preferences)
        viewModel = ViewModelProvider(requireActivity(), factory).get(MainViewModel::class.java)


        binding.viewModel = viewModel


        defineViews()

        return binding.root
    }

    fun defineViews() {

        initAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.addOnScrollListener(object : HidingViewScrollListener() {
            override fun onHide() {
                MainActivity.toolbar.visibility = View.GONE
            }

            override fun onShow() {
                MainActivity.toolbar.visibility = View.VISIBLE
            }
        })


        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (!isLoading) {
                if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                    false


                binding.recyclerView.visibility = View.VISIBLE


                val context = binding.recyclerView.context
                val layoutAnimationController =
                    AnimationUtils.loadLayoutAnimation(context, R.anim.layout_down_to_up)
                binding.recyclerView.layoutAnimation = layoutAnimationController
                displayArticles()

            } else {
                if (!binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                    true
                binding.recyclerView.visibility = View.INVISIBLE
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshResponse()
        }

    }


    private fun displayArticles() {

        viewModel.responseLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val articles = it.body()?.articles
                if (articles != null) {

                    if (binding.recyclerView.adapter is NewsTabAdapter)
                        newsTabAdapter.setArticleList(articles)
                    else
                        newsListAdapter.setArticleList(articles)

                }
            }
        })
    }

    private fun goToArticleDetailActivity(article: Article, imageView: ImageView) {
        val intent = Intent(requireContext(), ArticleDetailsActivity::class.java)
        intent.putExtra("article", ObjectSerializer.serialize(article))
        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            imageView,
            "article_image"
        )
        intent.putExtra("fab_visiblity", View.VISIBLE)
        startActivity(intent, activityOptions.toBundle())
    }


    private fun deleteBookmark(article: Article) {

        val bookmarks = viewModel.bookmarkList.value?.get(
            viewModel.responseLiveData.value?.body()!!.articles.indexOf(article)
        )
        if (bookmarks != null) {
            viewModel.deleteABookmark(bookmarks)
        }
    }


    private fun initAdapter(){

        newsListAdapter =   NewsListAdapter({ article: Article, imageView: ImageView ->
            goToArticleDetailActivity(article, imageView)
        }
            , {

            })

        newsTabAdapter =        NewsTabAdapter({ article: Article, imageView: ImageView ->
            goToArticleDetailActivity(article, imageView)
        }
            , { article ->

            })



        viewModel.appPrefrences.viewtype.asLiveData().observe(viewLifecycleOwner, Observer {viewType ->
            if (viewType.equals("List"))
                binding.recyclerView.adapter = newsListAdapter
            else
                binding.recyclerView.adapter = newsTabAdapter

        })

    }


}

