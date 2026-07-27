package com.capstone.planetku.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.capstone.planetku.MainActivity
import com.capstone.planetku.databinding.ActivityWelcomeBinding
import com.capstone.planetku.ui.LoginRegisterActivity
import androidx.core.content.edit

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val isFirstLaunch = sharedPreferences.getBoolean("IS_FIRST_LAUNCH", true)

        if (!isFirstLaunch) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences.edit {
            putBoolean("IS_FIRST_LAUNCH", false)
        }

        setupAnimations()

        binding.btnStarted.setOnClickListener {
            val intent = Intent(this, LoginRegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupAnimations() {
        binding.appLogoWelcome.alpha = 0f
        binding.tvWelcomeSelamat.alpha = 0f
        binding.tvWelcomeTitle.alpha = 0f
        binding.tvAppDescription.alpha = 0f
        binding.btnStarted.alpha = 0f
        binding.layoutFooter.alpha = 0f

        val logoFade = ObjectAnimator.ofFloat(binding.appLogoWelcome, View.ALPHA, 1f).setDuration(500)

        val titleFade = ObjectAnimator.ofFloat(binding.tvWelcomeSelamat, View.ALPHA, 1f).setDuration(500)
        val titleSlide = ObjectAnimator.ofFloat(binding.tvWelcomeSelamat, View.TRANSLATION_Y, 50f, 0f).setDuration(500)

        val subTitleFade = ObjectAnimator.ofFloat(binding.tvWelcomeTitle, View.ALPHA, 1f).setDuration(500)

        val descFade = ObjectAnimator.ofFloat(binding.tvAppDescription, View.ALPHA, 1f).setDuration(500)

        val btnFade = ObjectAnimator.ofFloat(binding.btnStarted, View.ALPHA, 1f).setDuration(500)
        val btnSlide = ObjectAnimator.ofFloat(binding.btnStarted, View.TRANSLATION_Y, 50f, 0f).setDuration(500)

        val footerFade = ObjectAnimator.ofFloat(binding.layoutFooter, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            play(logoFade)
            play(titleFade).with(titleSlide).after(logoFade)
            play(subTitleFade).after(titleFade)
            play(descFade).after(subTitleFade)
            play(btnFade).with(btnSlide).after(descFade)
            play(footerFade).after(btnFade)
            start()
        }
    }
}
