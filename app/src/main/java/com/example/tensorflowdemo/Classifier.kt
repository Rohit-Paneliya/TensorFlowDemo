package com.example.tensorflowdemo

import android.graphics.Bitmap

interface Classifier {

    fun recognizeImage(bitmap: Bitmap): List<Recognition>

    fun close()
}

class Recognition(
    val id: String?,
    /**
     * Display name for the recognition.
     */
    val title: String?,
    /**
     * A sortable score for how good the recognition is relative to others. Higher should be better.
     */
    val confidence: Float?,
    /**
     * Whether or not the model features quantized or float weights.
     */
    private val quant: Boolean
) {

    override fun toString(): String {
        var resultString = ""
//            if (id != null) {
//                resultString += "[$id] "
//            }

        if (title != null) {
            resultString += "$title "
        }

        if (confidence != null) {
            resultString += String.format("(%.1f%%) ", confidence * 100.0f)
        }

        return resultString.trim { it <= ' ' }
    }
}
