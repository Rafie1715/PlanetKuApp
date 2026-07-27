package com.capstone.planetku.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class ArticleRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val _articles = MutableLiveData<List<Article>?>()
    val articles: LiveData<List<Article>?> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchArticles() {
        _isLoading.value = true

        firestore.collection("articles")
            .get()
            .addOnSuccessListener { result ->
                _isLoading.value = false
                val articleList = result.toObjects(Article::class.java)
                
                val needsUpdate = articleList.size < 10 || (articleList[0].content.isEmpty())
                
                if (needsUpdate) {
                    FirestoreSeeder.seedArticles { success ->
                        if (success) fetchArticles()
                    }
                } else {
                    _articles.value = articleList
                }
            }
            .addOnFailureListener { exception ->
                _isLoading.value = false
                _error.value = "Gagal memuat berita: ${exception.message}"
                Log.e("ArticleRepo", "Error getting documents: ", exception)
            }
    }
}
