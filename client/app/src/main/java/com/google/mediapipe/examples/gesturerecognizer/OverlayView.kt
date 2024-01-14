package com.google.mediapipe.examples.gesturerecognizer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizerResult
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import kotlin.math.max
import kotlin.math.min

class OverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    private var results: GestureRecognizerResult? = null
    private var linePaint = Paint()
    private var pointPaint = Paint()

    private var isCameraFrontFacing: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    private var scaleFactor: Float = 1f
    private var imageWidth: Int = 1
    private var imageHeight: Int = 1

    init {
        initPaints()
    }

    fun clear() {
        results = null
        linePaint.reset()
        pointPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        linePaint.color =
            ContextCompat.getColor(context!!, R.color.color_primary)
        linePaint.strokeWidth = LANDMARK_STROKE_WIDTH
        linePaint.style = Paint.Style.STROKE

        pointPaint.color = Color.RED
        pointPaint.strokeWidth = LANDMARK_STROKE_WIDTH
        pointPaint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        results?.let { gestureRecognizerResult ->
            for (landmark in gestureRecognizerResult.landmarks()) {
                for (normalizedLandmark in landmark) {
                    val x: Float
                    if (isCameraFrontFacing) {
                        x = normalizedLandmark.x() * imageWidth * scaleFactor
                    } else {
                        x = (1 - normalizedLandmark.x()) * imageWidth * scaleFactor
                    }

                    canvas.drawPoint(
                        x,
                        normalizedLandmark.y() * imageHeight * scaleFactor,
                        pointPaint
                    )
                }

                // Draw hand connections with a different color and thicker stroke
                linePaint.color = ContextCompat.getColor(context!!, R.color.accent_color)
                linePaint.strokeWidth = HAND_CONNECTION_STROKE_WIDTH

                HandLandmarker.HAND_CONNECTIONS.forEach {
                    val startLandmark = gestureRecognizerResult.landmarks().get(0).get(it!!.start())
                    val endLandmark = gestureRecognizerResult.landmarks().get(0).get(it.end())

                    val startX: Float
                    val endX: Float

                    if (isCameraFrontFacing) {
                        startX = startLandmark.x() * imageWidth * scaleFactor
                        endX = endLandmark.x() * imageWidth * scaleFactor
                    } else {
                        startX = (1 - startLandmark.x()) * imageWidth * scaleFactor
                        endX = (1 - endLandmark.x()) * imageWidth * scaleFactor
                    }

                    canvas.drawLine(
                        startX,
                        startLandmark.y() * imageHeight * scaleFactor,
                        endX,
                        endLandmark.y() * imageHeight * scaleFactor,
                        linePaint
                    )
                }
            }
        }
    }

    fun setResults(
        gestureRecognizerResult: GestureRecognizerResult,
        imageHeight: Int,
        imageWidth: Int,
        runningMode: RunningMode = RunningMode.IMAGE
    ) {
        results = gestureRecognizerResult

        this.imageHeight = imageHeight
        this.imageWidth = imageWidth

        scaleFactor = when (runningMode) {
            RunningMode.IMAGE,
            RunningMode.VIDEO -> {
                min(width * 1f / imageWidth, height * 1f / imageHeight)
            }
            RunningMode.LIVE_STREAM -> {
                max(width * 1f / imageWidth, height * 1f / imageHeight)
            }
        }
        invalidate()
    }
    fun updateCameraFrontFacing(frontFacing: Boolean) {
        isCameraFrontFacing = frontFacing
    }
    companion object {
        private const val LANDMARK_STROKE_WIDTH = 20F
        private const val HAND_CONNECTION_STROKE_WIDTH = 12F
    }
}
