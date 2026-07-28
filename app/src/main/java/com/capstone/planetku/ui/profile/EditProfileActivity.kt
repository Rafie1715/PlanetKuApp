package com.capstone.planetku.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.bumptech.glide.Glide
import com.capstone.planetku.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import java.io.File
import java.io.FileOutputStream

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var currentImageUri: Uri? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            Glide.with(this).load(uri).into(binding.ivProfile)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupInitialData()
        setupListeners()
    }

    private fun setupInitialData() {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")
        val photoPath = sharedPreferences.getString("profile_photo_path", null)

        binding.etName.setText(name)
        
        if (photoPath != null) {
            Glide.with(this).load(File(photoPath)).into(binding.ivProfile)
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        binding.btnChangePhoto.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnSave.setOnClickListener {
            saveChanges()
        }
    }

    private fun saveChanges() {
        val newName = binding.etName.text.toString().trim()
        if (newName.isEmpty()) {
            binding.tilName.error = "Nama tidak boleh kosong"
            return
        }

        showLoading(true)

        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()

        user?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Update SharedPreferences
                val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
                sharedPreferences.edit {
                    putString("name", newName)
                }

                // If image changed, save it locally
                if (currentImageUri != null) {
                    saveImageLocally(currentImageUri!!)
                } else {
                    showLoading(false)
                    Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                showLoading(false)
                Toast.makeText(this, "Gagal memperbarui profil: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImageLocally(uri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File(filesDir, "profile_picture.jpg")
            val outputStream = FileOutputStream(file)
            
            inputStream?.copyTo(outputStream)
            
            inputStream?.close()
            outputStream.close()

            val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            sharedPreferences.edit {
                putString("profile_photo_path", file.absolutePath)
            }
            
            showLoading(false)
            Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            showLoading(false)
            Toast.makeText(this, "Gagal menyimpan foto: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnSave.isEnabled = !isLoading
    }
}
