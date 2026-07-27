package com.capstone.planetku.ui.classification

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageClassifierHelper(
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var interpreter: Interpreter? = null
    private var labels: List<String> = emptyList()

    init {
        setupInterpreter()
    }

    private fun setupInterpreter() {
        try {
            val modelBuffer = FileUtil.loadMappedFile(context, "model_sampah.tflite")
            labels = FileUtil.loadLabels(context, "labels.txt")
            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }
            interpreter = Interpreter(modelBuffer, options)
        } catch (e: Exception) {
            classifierListener?.onError("Gagal memuat model atau label: ${e.message}")
        }
    }

    fun classifyImage(bitmap: Bitmap) {
        if (interpreter == null) {
            setupInterpreter()
            if (interpreter == null) return
        }

        try {
            val inputShape = interpreter?.getInputTensor(0)?.shape()
            val inputHeight = inputShape?.get(1) ?: 224
            val inputWidth = inputShape?.get(2) ?: 224

            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(inputHeight, inputWidth, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(0f, 255f))
                .build()

            var tensorImage = TensorImage(org.tensorflow.lite.DataType.FLOAT32)
            tensorImage.load(bitmap)
            tensorImage = imageProcessor.process(tensorImage)

            val outputShape = interpreter?.getOutputTensor(0)?.shape()
            val numClasses = outputShape?.get(1) ?: labels.size
            
            val outputBuffer = ByteBuffer.allocateDirect(1 * numClasses * 4).apply {
                order(ByteOrder.nativeOrder())
            }

            interpreter?.run(tensorImage.buffer, outputBuffer)
            
            outputBuffer.rewind()
            val probabilities = FloatArray(numClasses)
            outputBuffer.asFloatBuffer().get(probabilities)

            var maxIdx = 0
            var maxProb = 0f
            for (i in probabilities.indices) {
                if (probabilities[i] > maxProb) {
                    maxProb = probabilities[i]
                    maxIdx = i
                }
            }

            val threshold = 0.5f
            if (maxProb >= threshold) {
                val label = if (maxIdx < labels.size) labels[maxIdx] else "Class $maxIdx"
                classifierListener?.onResults(label, maxProb)
            } else {
                classifierListener?.onResults("Objek tidak terdeteksi", maxProb)
            }

        } catch (e: Exception) {
            classifierListener?.onError("Inference error: ${e.message}")
        }
    }

    fun classifyImage(uri: android.net.Uri) {
        try {
            @Suppress("DEPRECATION")
            val bitmap = android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            classifyImage(bitmap)
        } catch (e: Exception) {
            classifierListener?.onError("Gagal memuat gambar: ${e.message}")
        }
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(result: String, confidence: Float)
    }
}
