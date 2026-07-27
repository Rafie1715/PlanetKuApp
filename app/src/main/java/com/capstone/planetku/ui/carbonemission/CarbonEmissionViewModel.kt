package com.capstone.planetku.ui.carbonemission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.planetku.R

class CarbonEmissionViewModel : ViewModel() {

    private val _totalEmission = MutableLiveData<Double>(0.0)
    val totalEmission: LiveData<Double> = _totalEmission

    private val _emissionStatus = MutableLiveData<EmissionStatus>()
    val emissionStatus: LiveData<EmissionStatus> = _emissionStatus

    fun calculate(
        electricity: String,
        transport: String,
        organic: String,
        inorganic: String
    ) {
        val e = electricity.toDoubleOrNull() ?: 0.0
        val t = transport.toDoubleOrNull() ?: 0.0
        val o = organic.toDoubleOrNull() ?: 0.0
        val i = inorganic.toDoubleOrNull() ?: 0.0

        val total = CarbonCalculator.calculateTotal(e, t, o, i)
        _totalEmission.value = total
        
        updateStatus(total)
    }

    private fun updateStatus(total: Double) {
        val status = when {
            total <= 100.0 -> EmissionStatus(
                "Rendah",
                "Jejak karbon Anda sangat baik! Pertahankan gaya hidup ramah lingkungan ini.",
                R.color.success
            )
            total <= 300.0 -> EmissionStatus(
                "Sedang",
                "Jejak karbon Anda rata-rata. Cobalah kurangi penggunaan listrik atau transportasi pribadi.",
                R.color.warning
            )
            else -> EmissionStatus(
                "Tinggi",
                "Peringatan! Jejak karbon Anda tinggi. Segera lakukan langkah pengurangan emisi.",
                R.color.error
            )
        }
        _emissionStatus.value = status
    }

    data class EmissionStatus(
        val label: String,
        val message: String,
        val colorRes: Int
    )
}
