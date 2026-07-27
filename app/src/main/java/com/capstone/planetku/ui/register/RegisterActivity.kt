package com.capstone.planetku.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.planetku.databinding.ActivityRegisterBinding
import com.capstone.planetku.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        playRegisterAnimation()
        setupAction()
    }

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (validateInput(name, email, password)) {
                performRegister(name, email, password)
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.nameEditTextLayout.error = "Nama tidak boleh kosong"
            isValid = false
        } else {
            binding.nameEditTextLayout.error = null
        }

        if (email.isEmpty()) {
            binding.emailEditTextLayout.error = "Email tidak boleh kosong"
            isValid = false
        } else {
            binding.emailEditTextLayout.error = null
        }

        if (password.isEmpty()) {
            binding.passwordEditTextLayout.error = "Password tidak boleh kosong"
            isValid = false
        } else if (password.length < 6) {
            binding.passwordEditTextLayout.error = "Password minimal 6 karakter"
            isValid = false
        } else {
            binding.passwordEditTextLayout.error = null
        }

        return isValid
    }

    private fun performRegister(name: String, email: String, password: String) {
        showLoading(true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            showLoading(false)
                            if (updateTask.isSuccessful) {
                                Toast.makeText(this, "Registrasi Berhasil! Silakan Login.", Toast.LENGTH_LONG).show()

                                auth.signOut()

                                val intent = Intent(this, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                                finish()
                            }
                        }
                } else {
                    showLoading(false)
                    Toast.makeText(this, "Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.registerButton.isEnabled = false
            binding.registerButton.text = "Loading..."
        } else {
            binding.progressBar.visibility = View.GONE
            binding.registerButton.isEnabled = true
            binding.registerButton.text = "Daftar"
        }
    }

    private fun playRegisterAnimation() {
        val views = listOf(
            binding.ivRegisterIllustration,
            binding.tvRegisterTitle,
            binding.tvRegisterSubtitle,
            binding.tvNameLabel,
            binding.nameEditTextLayout,
            binding.tvEmailLabel,
            binding.emailEditTextLayout,
            binding.tvPasswordLabel,
            binding.passwordEditTextLayout,
            binding.registerButton
        )
        views.forEach { it.alpha = 0f }

        val illustration = ObjectAnimator.ofFloat(binding.ivRegisterIllustration, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(500)
        val subtitle = ObjectAnimator.ofFloat(binding.tvRegisterSubtitle, View.ALPHA, 1f).setDuration(500)
        val nameL = ObjectAnimator.ofFloat(binding.tvNameLabel, View.ALPHA, 1f).setDuration(500)
        val nameI = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailL = ObjectAnimator.ofFloat(binding.tvEmailLabel, View.ALPHA, 1f).setDuration(500)
        val emailI = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passL = ObjectAnimator.ofFloat(binding.tvPasswordLabel, View.ALPHA, 1f).setDuration(500)
        val passI = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val btn = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            play(illustration).before(title)
            play(title).with(subtitle)
            play(nameL).after(subtitle)
            play(nameI).after(nameL)
            play(emailL).after(nameI)
            play(emailI).after(emailL)
            play(passL).after(emailI)
            play(passI).after(passL)
            play(btn).after(passI)
            start()
        }
    }
}
