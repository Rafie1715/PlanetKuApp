package com.capstone.planetku.ui.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.planetku.R
import com.capstone.planetku.data.Article

class ArticleAdapter(
    private val isHorizontal: Boolean = false,
    private val onItemClick: (Article) -> Unit
) : ListAdapter<Article, ArticleAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val layoutId = if (isHorizontal) R.layout.item_article_home else R.layout.item_article
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ArticleViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {

        private val tvTitle: android.widget.TextView = itemView.findViewById(R.id.tvTitle)
        private val tvExcerpt: android.widget.TextView = itemView.findViewById(R.id.tvExcerpt)
        private val tvDate: android.widget.TextView = itemView.findViewById(R.id.tvDate)
        private val ivArticle: android.widget.ImageView = itemView.findViewById(R.id.ivArticle)

        fun bind(article: Article) {
            tvTitle.text = article.title
            tvExcerpt.text = article.description
            tvDate.text = article.publishedAt.take(10)

            Glide.with(itemView.context)
                .load(article.urlToImage)
                .into(ivArticle)

            itemView.setOnClickListener { onItemClick(article) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }
        }
    }
}
