package com.android.segmentedprogresbar.progresslib

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.android.segmentedprogresbar.R

class ProgressBarModel(context: Context, attrs: AttributeSet?) {

    var segmentCount: Int = DEFAULT_SEGMENT_COUNT
    var containerColor: Int = Color.LTGRAY
    var fillColor: Int = Color.BLUE
    var segmentGapWidth: Int = context.dp(DEFAULT_SEGMENT_GAP_DP)
    var cornerRadius: Int = context.dp(DEFAULT_CORNER_RADIUS_DP)

    init {
        if (attrs != null) {
            val styledAttrs =
                context.theme.obtainStyledAttributes(attrs, R.styleable.SegmentedProgressBar, 0, 0)
            segmentCount =
                styledAttrs.getInt(R.styleable.SegmentedProgressBar_segment_count, 5)
            containerColor =
                styledAttrs.getColor(R.styleable.SegmentedProgressBar_container_color, containerColor)
            fillColor =
                styledAttrs.getColor(R.styleable.SegmentedProgressBar_fill_color, fillColor)
            segmentGapWidth =
                styledAttrs.getDimensionPixelSize(R.styleable.SegmentedProgressBar_gap_size, segmentGapWidth)
            cornerRadius =
                styledAttrs.getDimensionPixelSize(R.styleable.SegmentedProgressBar_corner_radius, cornerRadius)
        }
    }

    private companion object {
        const val DEFAULT_SEGMENT_COUNT = 5
        const val DEFAULT_CORNER_RADIUS_DP = 12
        const val DEFAULT_SEGMENT_GAP_DP = 2
    }

    fun Context.dp(dpVal: Int): Int = (dpVal * resources.displayMetrics.density).toInt()
}
