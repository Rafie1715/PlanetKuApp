package com.capstone.planetku.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.planetku.data.Article
import com.capstone.planetku.data.ArticleRepository

class ArticleViewModel : ViewModel() {

    private val repository = ArticleRepository()
    private val _articles = repository.articles
    val articles: LiveData<List<Article>?> = _articles

    private val _isLoading = repository.isLoading
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = repository.error
    val error: LiveData<String?> = _error

    fun fetchArticles() {
        repository.fetchArticles()
    }
}
