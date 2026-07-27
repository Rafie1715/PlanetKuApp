package com.capstone.planetku.ui.carbonemission

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.capstone.planetku.databinding.ActivityCarbonEmissionBinding
import java.util.Locale

class CarbonEmissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarbonEmissionBinding
    private val viewModel: CarbonEmissionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCarbonEmissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.totalEmission.observe(this) { total ->
            binding.tvTotalValue.text = String.format(Locale.getDefault(), "%.2f kg CO2", total)
        }

        viewModel.emissionStatus.observe(this) { status ->
            binding.cardStatus.visibility = View.VISIBLE
            binding.tvStatusLabel.text = status.label
            binding.tvStatusMessage.text = status.message
            
            val color = ContextCompat.getColor(this, status.colorRes)
            binding.viewStatusIndicator.backgroundTintList = ColorStateList.valueOf(color)
            binding.tvStatusLabel.setTextColor(color)
        }
    }

    private fun setupListeners() {
        binding.btnCalculate.setOnClickListener {
            val electricity = binding.etElectricity.text.toString()
            val transport = binding.etTransport.text.toString()
            val organic = binding.etOrganic.text.toString()
            val inorganic = binding.etInorganic.text.toString()

            viewModel.calculate(electricity, transport, organic, inorganic)
        }
    }
}
