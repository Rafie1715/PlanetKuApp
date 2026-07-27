package com.capstone.planetku.ui.priceprediction

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.planetku.data.PredictionResponse

class PricePredictionViewModel(application: Application) : AndroidViewModel(application) {

    private val _predictionResult = MutableLiveData<Result<PredictionResponse>>()
    val predictionResult: LiveData<Result<PredictionResponse>> = _predictionResult

    private val priceEstimatorHelper = PriceEstimatorHelper(application)

    fun predictPrice(
        wasteType: String,
        labelEncoded: Int,
        weight: Int,
        conditionEncoded: Int,
        locationEncoded: Int
    ) {
        val price = priceEstimatorHelper.predictPrice(
            labelEncoded,
            weight,
            conditionEncoded,
            locationEncoded
        )

        if (price >= 0) {
            _predictionResult.value = Result.success(
                PredictionResponse(item = wasteType, predictedPrice = price.toDouble())
            )
        } else {
            _predictionResult.value = Result.failure(Exception("Gagal menghitung estimasi harga"))
        }
    }

    override fun onCleared() {
        super.onCleared()
        priceEstimatorHelper.close()
    }
}
