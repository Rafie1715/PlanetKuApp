package com.capstone.planetku.ui.article

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.planetku.data.Article
import com.capstone.planetku.databinding.ActivityDetailArticleBinding
import androidx.core.net.toUri

class DetailArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        @Suppress("DEPRECATION")
        val article = intent.getParcelableExtra<Article>("EXTRA_ARTICLE")

        if (article != null) {
            setupUI(article)
        } else {
            finish()
        }
    }

    private fun setupUI(article: Article) {
        binding.apply {
            toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

            tvTitle.text = article.title
            tvDate.text = article.publishedAt
            tvContent.text = article.content

            Glide.with(this@DetailArticleActivity)
                .load(article.urlToImage)
                .into(ivCover)

            btnViewSource.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                startActivity(intent)
            }
        }
    }
}
