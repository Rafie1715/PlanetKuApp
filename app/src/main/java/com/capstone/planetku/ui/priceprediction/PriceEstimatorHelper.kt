package com.capstone.planetku.ui.priceprediction

import android.content.Context
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil

class PriceEstimatorHelper(
    val context: Context
) {
    private var interpreter: Interpreter? = null

    init {
        setupInterpreter()
    }

    private fun setupInterpreter() {
        try {
            val modelBuffer = FileUtil.loadMappedFile(context, "model_harga_sampah.tflite")
            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            interpreter = Interpreter(modelBuffer, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun predictPrice(
        labelEncoded: Int,
        weight: Int,
        conditionEncoded: Int,
        locationEncoded: Int
    ): Float {
        if (interpreter == null) {
            setupInterpreter()
        }

        val input = floatArrayOf(
            labelEncoded.toFloat(),
            weight.toFloat(),
            conditionEncoded.toFloat(),
            locationEncoded.toFloat()
        )
        
        val output = Array(1) { FloatArray(1) }
        
        try {
            interpreter?.run(arrayOf(input), output)
            return output[0][0]
        } catch (e: Exception) {
            e.printStackTrace()
            return -1f
        }
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }
}
