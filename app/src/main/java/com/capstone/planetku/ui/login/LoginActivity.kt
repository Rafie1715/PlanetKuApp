package com.capstone.planetku.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.capstone.planetku.MainActivity
import com.capstone.planetku.R
import com.capstone.planetku.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            navigateToMain()
        }

        playAnimation()
        setupAction()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailEditTextLayout.error = "Email tidak boleh kosong"
            } else {
                binding.emailEditTextLayout.error = null
            }

            if (password.isEmpty()) {
                binding.passwordEditTextLayout.error = "Password tidak boleh kosong"
            } else {
                binding.passwordEditTextLayout.error = null
            }

            if (email.isNotEmpty() && password.isNotEmpty()) {
                performLogin(email, password)
            }
        }

        binding.googleLoginButton.setOnClickListener {
            signInWithGoogle()
        }
    }

    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Toast.makeText(this, "Google Sign-In Gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        showLoading(true)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    saveUserData(user)
                    Toast.makeText(this, "Login Google Berhasil!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    Toast.makeText(this, "Autentikasi Firebase Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun performLogin(email: String, pass: String) {
        showLoading(true)

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                showLoading(false)

                if (task.isSuccessful) {
                    val user = auth.currentUser
                    saveUserData(user)
                    Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    Toast.makeText(this, "Login Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserData(user: FirebaseUser?) {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString("name", user?.displayName ?: "Pengguna")
            putString("email", user?.email)
            putBoolean("IS_FIRST_LAUNCH", false)
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !isLoading
        binding.loginButton.text = if (isLoading) "Loading..." else "Masuk"
    }

    private fun playAnimation() {
        val views = listOf(
            binding.ivLoginIllustration,
            binding.tvLoginTitle,
            binding.tvLoginSubtitle,
            binding.tvEmailLabel,
            binding.emailEditTextLayout,
            binding.tvPasswordLabel,
            binding.passwordEditTextLayout,
            binding.loginButton,
            binding.googleLoginButton
        )
        views.forEach { it.alpha = 0f }

        val illustration = ObjectAnimator.ofFloat(binding.ivLoginIllustration, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(500)
        val subtitle = ObjectAnimator.ofFloat(binding.tvLoginSubtitle, View.ALPHA, 1f).setDuration(500)
        val emailL = ObjectAnimator.ofFloat(binding.tvEmailLabel, View.ALPHA, 1f).setDuration(500)
        val emailI = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passL = ObjectAnimator.ofFloat(binding.tvPasswordLabel, View.ALPHA, 1f).setDuration(500)
        val passI = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val btn = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val googleBtn = ObjectAnimator.ofFloat(binding.googleLoginButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            play(illustration).before(title)
            play(title).with(subtitle)
            play(emailL).after(subtitle)
            play(emailI).after(emailL)
            play(passL).after(emailI)
            play(passI).after(passL)
            play(btn).after(passI)
            play(googleBtn).after(btn)
            start()
        }
    }
}
