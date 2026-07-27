package com.capstone.planetku.ui.classification

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.capstone.planetku.CameraActivity
import com.capstone.planetku.databinding.ActivityWasteClassificationBinding
import com.capstone.planetku.ui.priceprediction.PricePredictionActivity

class WasteClassificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWasteClassificationBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private var currentResult: String? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCameraActivity()
            } else {
                Toast.makeText(this, "Izin kamera diperlukan untuk menggunakan fitur ini", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWasteClassificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClassifier()

        binding.btnCapture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startCameraActivity()
            } else {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }

        val galleryLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    binding.ivResult.setImageURI(uri)
                    binding.ivResult.visibility = View.VISIBLE
                    binding.tvPlaceholder.visibility = View.GONE
                    binding.cardResult.visibility = View.GONE
                    imageClassifierHelper.classifyImage(uri)
                } else {
                    Toast.makeText(this, "Gagal memilih gambar dari galeri", Toast.LENGTH_SHORT).show()
                }
            }

        binding.btnGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnPredict.setOnClickListener {
            currentResult?.let { result ->
                navigateToPricePrediction(result)
            } ?: run {
                Toast.makeText(this, "Silakan masukkan gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCameraActivity() {
        val intent = Intent(this, CameraActivity::class.java)
        cameraLauncher.launch(intent)
    }

    private fun setupClassifier() {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Toast.makeText(this@WasteClassificationActivity, "Error: $error", Toast.LENGTH_SHORT).show()
                }

                override fun onResults(result: String, confidence: Float) {
                    currentResult = result
                    binding.tvResult.text = "Terdeteksi: $result (${(confidence * 100).toInt()}%)"
                    binding.cardResult.visibility = View.VISIBLE
                    
                    if (result != "Objek tidak terdeteksi") {
                        updateUserStats()
                    }
                }
            }
        )
    }

    private fun updateUserStats() {
        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val currentTotal = sharedPreferences.getInt("total_waste_sorted", 0)
        val currentCarbon = sharedPreferences.getFloat("total_carbon_saved", 0.0f)

        sharedPreferences.edit {
            putInt("total_waste_sorted", currentTotal + 1)
            putFloat("total_carbon_saved", currentCarbon + 0.1f)
        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val photoPath = result.data?.getStringExtra("PHOTO_PATH")
                if (!photoPath.isNullOrEmpty()) {
                    val bitmap = BitmapFactory.decodeFile(photoPath)
                    binding.ivResult.setImageBitmap(bitmap)
                    binding.ivResult.visibility = View.VISIBLE
                    binding.tvPlaceholder.visibility = View.GONE
                    binding.cardResult.visibility = View.GONE
                    imageClassifierHelper.classifyImage(bitmap)
                }
            } else {
                Toast.makeText(this, "Pengambilan gambar dibatalkan", Toast.LENGTH_SHORT).show()
            }
        }

    private fun navigateToPricePrediction(classifiedWaste: String) {
        val intent = Intent(this, PricePredictionActivity::class.java)
        intent.putExtra("CLASSIFIED_WASTE", classifiedWaste)
        startActivity(intent)
    }
}
