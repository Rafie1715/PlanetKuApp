package com.capstone.planetku.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.capstone.planetku.databinding.ActivityLoginRegisterBinding
import com.capstone.planetku.ui.login.LoginActivity
import com.capstone.planetku.ui.register.RegisterActivity

class LoginRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun playAnimation() {
        binding.ivIllustration.alpha = 0f
        binding.tvTitle.alpha = 0f
        binding.tvSubtitle.alpha = 0f
        binding.btnLogin.alpha = 0f
        binding.btnRegister.alpha = 0f

        val illustration = ObjectAnimator.ofFloat(binding.ivIllustration, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val subtitle = ObjectAnimator.ofFloat(binding.tvSubtitle, View.ALPHA, 1f).setDuration(500)
        val loginBtn = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val registerBtn = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            play(illustration).before(title)
            play(title).with(subtitle)
            play(loginBtn).after(subtitle)
            play(registerBtn).after(loginBtn)
            start()
        }
    }
}
