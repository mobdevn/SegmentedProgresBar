package com.android.segmentedprogresbar.progresslib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class SegmentedProgressBarView : View {

    private lateinit var containerRectanglePaint: Paint
    private lateinit var fillRectanglePaint: Paint
    private lateinit var properties: ProgressBarModel

    private var lastCompletedSegment = 0
    private var currentSegmentProgressInPx = 0

    private var segmentCompletedListener: CompletedSegmentListener? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet? = null) {
        initPropertiesModel(attrs)
        containerRectanglePaint = fillSegmentsPaint(properties.containerColor)
        fillRectanglePaint = fillSegmentsPaint(properties.fillColor)
    }

    private fun initPropertiesModel(attrs: AttributeSet?) {
        properties = ProgressBarModel(context, attrs)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawEmptySegments(canvas)
        drawProgressSegments(canvas)
    }

    /**
     * Set the color of the the progress bar when unfilled/idle state
     */
    @Suppress("Unused")
    fun setContainerColor(@ColorInt color: Int) {
        containerRectanglePaint = fillSegmentsPaint(color)
    }

    /**
     * Set the color of the progress bar when filled or in-progress state
     */
    @Suppress("Unused")
    fun setFillColor(@ColorInt color: Int) {
        fillRectanglePaint = fillSegmentsPaint(color)
    }

    /**
     * Set the total number of segments in the progress bar.
     */
    @Suppress("Unused")
    fun setSegmentCount(segmentCount: Int) {
        properties.segmentCount = segmentCount
    }

    /**
     * Callback that will be triggered when a segment is filled.
     */
    @Suppress("Unused")
    fun setCompletedSegmentListener(listener: CompletedSegmentListener) {
        this.segmentCompletedListener = listener
    }

    /**
     * increment the progress bar as when there is update in view
     */
    fun incrementCompletedSegments() {
        if (lastCompletedSegment <= properties.segmentCount * 2) {
            currentSegmentProgressInPx = 0
            lastCompletedSegment++
            invalidate()
            segmentCompletedListener?.onSegmentCompleted(lastCompletedSegment)
        }
    }

    /**
     * We can reset and also decrement progress on back press or back events or
     * directly passing the values in the range of [0..number-of segment-counts]
     */
    fun setCompletedSegments(completedSegments: Int) {
        if (completedSegments < 0) return
        if (completedSegments <= properties.segmentCount * 2) {
            currentSegmentProgressInPx = 0
            lastCompletedSegment = completedSegments
            invalidate()
            segmentCompletedListener?.onSegmentCompleted(lastCompletedSegment)
        }
    }

    private fun drawEmptySegments(canvas: Canvas) {
        val segmentWidth = segmentWidth

        var leftX = 0
        var rightX = leftX + segmentWidth
        val topY = 0
        val botY = height

        for (i in 0 until properties.segmentCount) {
            drawSegmentView(canvas, leftX.toFloat(), topY.toFloat(), rightX.toFloat(), botY.toFloat(),
                containerRectanglePaint)
            leftX += segmentWidth + properties.segmentGapWidth
            rightX = leftX + segmentWidth
        }
    }

    private fun drawProgressSegments(canvas: Canvas) {
        val segmentWidth = segmentWidth/2

        var leftX = 0
        var rightX = leftX + segmentWidth
        val topY = 0
        val botY = height

        for (i in 0 until lastCompletedSegment) {
            drawSegmentView(canvas, leftX.toFloat(), topY.toFloat(), rightX.toFloat(), botY.toFloat(), fillRectanglePaint)
            val index = i + 1
            if (index >= 2 && (index % 2 == 0)) {
                leftX += segmentWidth + properties.segmentGapWidth
            } else {
                leftX += segmentWidth
            }

            rightX = segmentWidth + leftX
        }
    }

    private fun drawSegmentView(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        paint: Paint
    ) {

        val path = Path()
        var rx = properties.cornerRadius.toFloat()
        if (rx < 0) rx = 0f
        var ry = 6f
        if (ry < 0) ry = 0f
        val width = right - left
        val height = bottom - top
        if (rx > width / 2) rx = width / 2
        if (ry > height / 2) ry = height / 2
        val widthMinusCorners = width - 2 * rx
        val heightMinusCorners = height - 2 * ry

        with(path) {
            moveTo(right, top + ry)
            rQuadTo(0f, -ry, -rx, -ry) // top-right corner
            rLineTo(-widthMinusCorners, 0f)
            rQuadTo(-rx, 0f, -rx, ry) // top-left corner
            rLineTo(0f, heightMinusCorners)

            rQuadTo(0f, ry, rx, ry) // bottom-left corner
            rLineTo(widthMinusCorners, 0f)
            rQuadTo(rx, 0f, rx, -ry) // bottom-right corner

            rLineTo(0f, -heightMinusCorners)
            close()
        }

        canvas.drawPath(path, paint)
    }

    private fun fillSegmentsPaint(@ColorInt color: Int): Paint {
        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.FILL
        return paint
    }

    private val segmentWidth: Int
        get() = width / properties.segmentCount
}

interface CompletedSegmentListener {
    fun onSegmentCompleted(segmentCount: Int)
}