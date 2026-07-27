package com.capstone.planetku.ui.priceprediction

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.planetku.databinding.ActivityPricePredictionBinding

class PricePredictionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPricePredictionBinding
    
    private val wasteTypes = listOf(
        "battery", "cardboard", "clothes", "glass", "metal", "paper", "plastic", "shoes", "styrofoam"
    )
    
    private val conditions = listOf("Baik", "Buruk", "Sedang")
    
    private val locations = listOf("Bandung", "Jakarta", "Surabaya")

    private val viewModel: PricePredictionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPricePredictionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val classifiedWaste = intent.getStringExtra("CLASSIFIED_WASTE")
        setupSpinners(classifiedWaste)
        setupObservers()

        binding.btnPredictPrice.setOnClickListener {
            val weightStr = binding.etWeight.text.toString()
            if (weightStr.isEmpty()) {
                binding.etWeight.error = "Masukkan berat sampah"
                return@setOnClickListener
            }

            val weight = weightStr.toInt()
            val labelEncoded = binding.spWasteType.selectedItemPosition
            val conditionEncoded = binding.spCondition.selectedItemPosition
            val locationEncoded = binding.spLocation.selectedItemPosition
            val selectedWasteType = binding.spWasteType.selectedItem.toString()

            binding.progressBar.visibility = View.VISIBLE
            binding.cardResult.visibility = View.GONE

            viewModel.predictPrice(
                selectedWasteType,
                labelEncoded,
                weight,
                conditionEncoded,
                locationEncoded
            )
        }
    }

    private fun setupSpinners(defaultWasteType: String?) {
        val wasteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, wasteTypes)
        wasteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spWasteType.adapter = wasteAdapter

        defaultWasteType?.let {
            val position = wasteTypes.indexOf(it.lowercase())
            if (position >= 0) {
                binding.spWasteType.setSelection(position)
            }
        }

        val conditionAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, conditions)
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCondition.adapter = conditionAdapter

        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, locations)
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spLocation.adapter = locationAdapter
    }

    private fun setupObservers() {
        viewModel.predictionResult.observe(this) { result ->
            binding.progressBar.visibility = View.GONE

            result.onSuccess { prediction ->
                binding.cardResult.visibility = View.VISIBLE
                binding.tvPredictionResult.text = "Rp ${prediction.predictedPrice?.toInt() ?: 0}"
                
                Toast.makeText(this, "Estimasi berhasil dihitung", Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                binding.cardResult.visibility = View.VISIBLE
                binding.tvPredictionResult.text = "Error"
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
