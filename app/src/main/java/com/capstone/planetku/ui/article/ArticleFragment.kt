package com.capstone.planetku.ui.article

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.planetku.data.Article
import com.capstone.planetku.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val articleViewModel: ArticleViewModel by viewModels()
    private lateinit var adapter: ArticleAdapter

    private var originalList: List<Article> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        setupSwipeRefresh()
        observeViewModel()

        articleViewModel.fetchArticles()
    }

    private fun setupRecyclerView() {
        adapter = ArticleAdapter(isHorizontal = false) { article ->
            val intent = Intent(requireContext(), DetailArticleActivity::class.java)
            intent.putExtra("EXTRA_ARTICLE", article)
            startActivity(intent)
        }

        binding.rvArticles.layoutManager = LinearLayoutManager(requireContext())
        binding.rvArticles.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    filterList(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        if (query.isNullOrEmpty()) {
            adapter.submitList(originalList)
        } else {
            val filteredList = originalList.filter { item ->
                item.title.contains(query, ignoreCase = true) || item.description.contains(query, ignoreCase = true)
            }
            adapter.submitList(filteredList)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            articleViewModel.fetchArticles()
        }
    }

    private fun observeViewModel() {
        articleViewModel.articles.observe(viewLifecycleOwner) { articleList ->
            if (!articleList.isNullOrEmpty()) {
                originalList = articleList
                adapter.submitList(articleList)
                binding.tvError.visibility = View.GONE
            } else {
                if (articleList != null) {
                    Toast.makeText(requireContext(), "Tidak ada artikel ditemukan", Toast.LENGTH_SHORT).show()
                }
                binding.tvError.visibility = View.VISIBLE
                adapter.submitList(emptyList())
            }
        }

        articleViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (!binding.swipeRefresh.isRefreshing) {
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
            if (!isLoading) binding.swipeRefresh.isRefreshing = false
        }

        articleViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
