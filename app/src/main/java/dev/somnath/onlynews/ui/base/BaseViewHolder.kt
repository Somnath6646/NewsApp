package dev.somnath.onlynews.ui.base

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.somnath.onlynews.R
import dev.somnath.onlynews.model.Article

abstract class BaseViewHolder<viewDataBinding : ViewDataBinding>(private val binding: viewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {


    protected abstract val titleArticle: TextView
    protected abstract val publishedAt: TextView
    protected abstract val source: TextView
    protected abstract val image: ImageView
    protected abstract val viewLayout: ConstraintLayout


    fun bind(
        article: Article,
        clickListener: (Article, ImageView) -> Unit,
        longClickListener: (Article) -> Unit
    ) {

        if (article.urlToImage.isNullOrEmpty()) {
            image.setImageResource(R.drawable.ic_logo_onlynews)
        } else {

            Picasso.get()
                .load(article.urlToImage)
                .fit()
                .centerCrop()
                .into(image)
        }

        titleArticle.text = article.title
        publishedAt.text =
            "${article.publishedAt!!.substring(0, article.publishedAt!!.indexOf('T'))}"
        source.text = article.source.name


        viewLayout.setOnClickListener {
            clickListener(article, image)
        }

        viewLayout.setOnLongClickListener {
            longClickListener(article)
            true
        }

    }
}