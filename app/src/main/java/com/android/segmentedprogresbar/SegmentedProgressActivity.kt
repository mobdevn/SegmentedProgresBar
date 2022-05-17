package com.android.segmentedprogresbar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.android.segmentedprogresbar.progresslib.CompletedSegmentListener
import com.android.segmentedprogresbar.progresslib.SegmentedProgressBarView

class SegmentedProgressActivity : AppCompatActivity() {
    private var lastSegmentCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_segmented_progress)

        val segmentedProgressBar = findViewById<SegmentedProgressBarView>(R.id.segmentProgressBar)
        segmentedProgressBar.setSegmentCount(4)
        segmentedProgressBar.setContainerColor(Color.LTGRAY)
        segmentedProgressBar.setFillColor(Color.MAGENTA)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            segmentedProgressBar.incrementCompletedSegments()
        }

        findViewById<Button>(R.id.btnPause).setOnClickListener {
            segmentedProgressBar.setCompletedSegments(lastSegmentCount - 1)
        }

        segmentedProgressBar.setCompletedSegmentListener(object : CompletedSegmentListener {
            override fun onSegmentCompleted(segmentCount: Int) {
                lastSegmentCount = segmentCount
            }
        })
    }
}