package com.capstone.planetku.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.planetku.R
import com.capstone.planetku.databinding.FragmentHomeBinding
import com.capstone.planetku.ui.article.ArticleAdapter
import com.capstone.planetku.ui.article.ArticleViewModel
import com.capstone.planetku.ui.article.DetailArticleActivity
import com.capstone.planetku.ui.carbonemission.CarbonEmissionActivity
import com.capstone.planetku.ui.classification.WasteClassificationActivity
import com.capstone.planetku.ui.priceprediction.PricePredictionActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val articleViewModel: ArticleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGreeting()
        setupUserStats()
        setupMenuActions()
        setupArticleList()
        playHomeAnimations()
    }

    override fun onResume() {
        super.onResume()
        setupUserStats()
    }

    private fun setupGreeting() {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("name", "Sobat PlanetKu") ?: "Sobat PlanetKu"
        val firstName = userName.split(" ").firstOrNull() ?: userName
        binding.tvGreeting.text = "Halo, $firstName!"
    }

    private fun setupUserStats() {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val totalWaste = sharedPreferences.getInt("total_waste_sorted", 0)
        val savedCarbon = sharedPreferences.getFloat("total_carbon_saved", 0.0f)

        binding.tvTotalWaste.text = totalWaste.toString()
        binding.tvSavedCarbon.text = String.format(java.util.Locale.getDefault(), "%.1f kg", savedCarbon)
    }

    private fun setupMenuActions() {
        binding.btnMenuScan.setOnClickListener {
            startActivity(Intent(requireContext(), WasteClassificationActivity::class.java))
        }

        binding.btnMenuCarbon.setOnClickListener {
            startActivity(Intent(requireContext(), CarbonEmissionActivity::class.java))
        }

        binding.btnMenuPrice.setOnClickListener {
            startActivity(Intent(requireContext(), PricePredictionActivity::class.java))
        }

        binding.btnMenuMaps.setOnClickListener {
            try {
                findNavController().navigate(R.id.navigation_maps)
            } catch (_: Exception) {
                Toast.makeText(requireContext(), "Membuka Peta...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupArticleList() {
        val adapter = ArticleAdapter(isHorizontal = true) { article ->
            val intent = Intent(requireContext(), DetailArticleActivity::class.java)
            intent.putExtra("EXTRA_ARTICLE", article)
            startActivity(intent)
        }

        binding.rvArticles.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticles.adapter = adapter
        binding.rvArticles.setHasFixedSize(true)

        articleViewModel.fetchArticles()

        articleViewModel.articles.observe(viewLifecycleOwner) { articleList ->
            if (articleList != null) {
                adapter.submitList(articleList)
            }
        }

        articleViewModel.isLoading.observe(viewLifecycleOwner) { _ ->
        }

        articleViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), "Gagal memuat: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playHomeAnimations() {
        val greeting = binding.tvGreeting
        val subtitle = binding.tvSubtitle
        val profile = binding.ivProfile
        val statsCard = binding.cardStats
        val menuGrid = binding.gridMenu
        val articleTitle = binding.tvArticleTitle
        val articleList = binding.rvArticles

        val views = listOf(greeting, subtitle, profile, statsCard, menuGrid, articleTitle, articleList)
        views.forEach { it.alpha = 0f }

        val animGreeting = ObjectAnimator.ofFloat(greeting, View.ALPHA, 0f, 1f).setDuration(500)
        val animSubtitle = ObjectAnimator.ofFloat(subtitle, View.ALPHA, 0f, 1f).setDuration(500)

        val animStats = ObjectAnimator.ofFloat(statsCard, View.ALPHA, 0f, 1f).setDuration(500)
        val slideStats = ObjectAnimator.ofFloat(statsCard, View.TRANSLATION_Y, 50f, 0f).setDuration(500)

        val animMenu = ObjectAnimator.ofFloat(menuGrid, View.ALPHA, 0f, 1f).setDuration(500)
        val slideMenu = ObjectAnimator.ofFloat(menuGrid, View.TRANSLATION_Y, 50f, 0f).setDuration(500)

        val animArticleTitle = ObjectAnimator.ofFloat(articleTitle, View.ALPHA, 0f, 1f).setDuration(500)
        val animArticleList = ObjectAnimator.ofFloat(articleList, View.ALPHA, 0f, 1f).setDuration(500)

        AnimatorSet().apply {
            play(animGreeting).with(animSubtitle)
            play(animStats).with(slideStats).after(animGreeting)
            play(animMenu).with(slideMenu).after(animStats)
            play(animArticleTitle).after(animMenu)
            play(animArticleList).after(animArticleTitle)
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
