package com.capstone.planetku

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.capstone.planetku.databinding.ActivityMainBinding
import com.capstone.planetku.ui.article.ArticleFragment
import com.capstone.planetku.ui.maps.MapsFragment
import com.capstone.planetku.ui.more.MoreFragment
import com.capstone.planetku.ui.home.HomeFragment
import com.capstone.planetku.ui.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> loadFragment(HomeFragment())
                R.id.menu_map -> loadFragment(MapsFragment())
                R.id.menu_article -> loadFragment(ArticleFragment())
                R.id.menu_more -> loadFragment(MoreFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
