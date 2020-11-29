package dev.somnath.onlynews.ui.articledetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dev.somnath.onlynews.utils.ObjectSerializer
import dev.somnath.onlynews.R
import dev.somnath.onlynews.model.Article
import dev.somnath.onlynews.databinding.ActivityArticleDetailsBinding
import dev.somnath.onlynews.ui.base.BaseActivity
import dev.somnath.onlynews.ui.main.MainViewModel
import kotlinx.android.synthetic.main.article_content.view.*


class ArticleDetailsActivity : BaseActivity<ActivityArticleDetailsBinding, MainViewModel, ArticleDetailsActivity>() {

    private lateinit var menu: Menu
    private lateinit var article:Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.elevation = 0f

        article = ObjectSerializer.deserialize(intent.getStringExtra("article")) as Article


        setUpLayout()



    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            this.menu = menu
        }
        menuInflater.inflate(R.menu.article_menu, menu)
        hideOptions(R.id.action_bookmark)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_bookmark ->{
                Snackbar.make(binding.includedLayout,"Added to Bookmark", Snackbar.LENGTH_SHORT ).show()
                viewModel.addABookmark(article = article)
            }
        }
        return true
    }

    fun showOptions(id: Int){
        val item = menu.findItem(id)
        item.setVisible(intent.getIntExtra("fab_visiblity", View.GONE) == View.VISIBLE)
    }

    fun hideOptions(id: Int){
        val item = menu.findItem(id)
        item.setVisible(false)
    }

    fun setUpLayout(){


        // filling every views
        if(article.urlToImage.isNullOrEmpty()){

        }else {
            binding.expandedImage.visibility = View.VISIBLE
            Picasso.get()
                .load(article.urlToImage)
                .fit()
                .centerCrop()
                .into(binding.expandedImage)
        }

        binding.includedLayout.apply {
            articleTitle.text = article.title
            if(article.content.toString().isEmpty()) article.content = ""
            articleContent.text = article.content +"\n\n\n"+
                    "Full article at : "+article.url
        }

        //handling events
        binding.fabBookmark.visibility = intent.getIntExtra("fab_visiblity", View.GONE)

        binding.fabBookmark.setOnClickListener{
            Snackbar.make(it,"Added to Bookmark", Snackbar.LENGTH_SHORT ).show()
            viewModel.addABookmark(article = article)
        }

        binding.shareButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/web"
            intent.putExtra(Intent.EXTRA_TEXT, article.title + "\n"+article.url)
            val shareIntent = Intent.createChooser(intent, "Share via")
            startActivity(shareIntent)
        }

        binding.appBar.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    showOptions(R.id.action_bookmark)
                } else if (isShow) {
                    isShow = false
                    hideOptions(R.id.action_bookmark)
                }
            }
        })

    }

    override fun getViewBinding(): ActivityArticleDetailsBinding = ActivityArticleDetailsBinding.inflate(layoutInflater)
    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java
    override fun getViewModelStoreOwner(): ArticleDetailsActivity = this
    override fun getContext(): Context = this
}