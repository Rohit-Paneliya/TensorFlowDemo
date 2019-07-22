package com.example.tensorflowdemo

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wonderkiln.camerakit.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var classifier: Classifier? = null

    private val MODEL_PATH = "mobilenet_quant_v1_224.tflite"
    private val LABEL_PATH = "labels.txt"
    private val IMAGE_SIZE = 224

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initCameraView()
        initTensorFlowAndLoadModel()

        btnDetectObject.setOnClickListener { cameraView.captureImage() }
    }

    private fun initTensorFlowAndLoadModel() {

        Handler().post {
            try {
                classifier = TensorFlowImageClassifier.create(
                    assets,
                    MODEL_PATH,
                    LABEL_PATH,
                    IMAGE_SIZE
                )
                makeButtonVisible()
            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }

    private fun initCameraView() {
        cameraView.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) {

            }

            override fun onError(cameraKitError: CameraKitError) {

            }

            override fun onImage(cameraKitImage: CameraKitImage) {

                var bitmap = cameraKitImage.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, IMAGE_SIZE, IMAGE_SIZE, false)
                imageViewResult.setImageBitmap(bitmap)

                //Call recognize function.
                val results = classifier?.recognizeImage(bitmap)

                var item = ""
                results?.forEach {
                    item += it.title + ": " + String.format("(%.1f%%) ", it.confidence ?: 0f * 100.0f) + "\n"
                }
                textViewResult.text = item

            }

            override fun onVideo(cameraKitVideo: CameraKitVideo) {

            }
        })
    }

    private fun makeButtonVisible() {
        runOnUiThread { btnDetectObject.visibility = View.VISIBLE }
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        cameraView.stop()
        super.onPause()
    }
}
