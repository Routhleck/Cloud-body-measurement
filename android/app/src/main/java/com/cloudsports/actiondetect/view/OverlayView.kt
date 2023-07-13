package com.cloudsports.actiondetect.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.cloudsports.actiondetect.R
import com.cloudsports.actiondetect.algorithm.MainActionCount
import com.cloudsports.actiondetect.debug.ToastDebug
import com.cloudsports.actiondetect.model.DetectViewModel
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlin.math.max
import kotlin.math.min

@SuppressLint("ViewConstructor")
class OverlayView(
    context: Context?,
    attrs: AttributeSet?) :
    View(context, attrs) {

    private var results: PoseLandmarkerResult? = null
    private var pointPaint = Paint()
    private var linePaint = Paint()

    private var scaleFactor: Float = 1f
    private var imageWidth: Int = 1
    private var imageHeight: Int = 1

    private var lastCount = 0
    private var count = 0
    private lateinit var mainActionCount: MainActionCount

    private var toast: ToastDebug? = ToastDebug(context)

    private lateinit var actionName: String

    private lateinit var viewModel: DetectViewModel

    private var isStarted = false
    private var isStopped = false

    private val detectSound = MediaPlayer.create(context, R.raw.action_detect)


    init {
        initPaints()
    }

    fun setViewModel(viewModel: DetectViewModel) {
        this.viewModel = viewModel
        (context as? LifecycleOwner)?.let { owner ->
            viewModel.actionName.observe(owner) { name ->
                actionName = name!!
            }
        }
        actionName = viewModel.actionName.value!!
        mainActionCount = MainActionCount(context, actionName)
    }

    fun start() {
        isStarted = true
        isStopped = false
    }

    fun reset() {
        mainActionCount = MainActionCount(context, actionName)
        isStarted = false
        isStopped = false
        lastCount = 0
        count = 0
    }

    fun stop() {
        isStopped = true
    }

    fun getCount(): Int {
        return count
    }

    fun clear() {
        results = null
        pointPaint.reset()
        linePaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        linePaint.color =
            ContextCompat.getColor(context!!, R.color.mp_color_primary)
        linePaint.strokeWidth = LANDMARK_STROKE_WIDTH
        linePaint.style = Paint.Style.STROKE

        pointPaint.color = Color.YELLOW
        pointPaint.strokeWidth = LANDMARK_STROKE_WIDTH
        pointPaint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        results?.let { poseLandmarkerResult ->
            for(landmark in poseLandmarkerResult.landmarks()) {
                for(normalizedLandmark in landmark) {
                    canvas.drawPoint(
                        normalizedLandmark.x() * imageWidth * scaleFactor,
                        normalizedLandmark.y() * imageHeight * scaleFactor,
                        pointPaint
                    )
                }
                PoseLandmarker.POSE_LANDMARKS.forEach {
                    canvas.drawLine(
                        poseLandmarkerResult.landmarks()[0][it!!.start()]
                            .x() * imageWidth * scaleFactor,
                        poseLandmarkerResult.landmarks()[0][it.start()]
                            .y() * imageHeight * scaleFactor,
                        poseLandmarkerResult.landmarks()[0][it.end()]
                            .x() * imageWidth * scaleFactor,
                        poseLandmarkerResult.landmarks()[0][it.end()]
                            .y() * imageHeight * scaleFactor,
                        linePaint
                    )
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun setResults(
        poseLandmarkerResults: PoseLandmarkerResult,
        imageHeight: Int,
        imageWidth: Int,
        runningMode: RunningMode = RunningMode.IMAGE
    ) {
        results = poseLandmarkerResults

        this.imageHeight = imageHeight
        this.imageWidth = imageWidth

        // 将results.landmarks()转化为Array<DoubleArray>
        val poseLandmarks = Array(33) { DoubleArray(3) }

        if (isStarted) {
            if (!isStopped) {
                if (poseLandmarkerResults.landmarks().isNotEmpty()) {
                    for (i in 0..32) {
                        poseLandmarks[i][0] = poseLandmarkerResults.landmarks()[0][i].x().toDouble()
                        poseLandmarks[i][1] = poseLandmarkerResults.landmarks()[0][i].y().toDouble()
                        poseLandmarks[i][2] = poseLandmarkerResults.landmarks()[0][i].z().toDouble()
                    }
                    lastCount = count
                    count = mainActionCount(poseLandmarks, imageHeight, imageWidth)
                    if (lastCount != count) {
                        // 将count传递给DetectViewModel
                        viewModel.setCount(count)
                        // 播放音效
                        detectSound.start()
                    }
                }
            }
        }

        scaleFactor = when (runningMode) {
            RunningMode.IMAGE,
            RunningMode.VIDEO -> {
                min(width * 1f / imageWidth, height * 1f / imageHeight)
            }
            RunningMode.LIVE_STREAM -> {
                // PreviewView is in FILL_START mode. So we need to scale up the
                // landmarks to match with the size that the captured images will be
                // displayed.
                max(width * 1f / imageWidth, height * 1f / imageHeight)
            }
        }
        invalidate()
    }

    companion object {
        private const val LANDMARK_STROKE_WIDTH = 12F
    }
}