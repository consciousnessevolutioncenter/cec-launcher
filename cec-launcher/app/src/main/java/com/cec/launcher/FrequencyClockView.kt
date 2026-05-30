package com.cec.launcher

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FrequencyClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val solfeggioFrequencies = listOf(
        "396 Hz · Liberation", "417 Hz · Change", "528 Hz · Miracle",
        "639 Hz · Connection", "741 Hz · Awakening", "852 Hz · Intuition",
        "963 Hz · Divine"
    )

    private val timePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        textAlign = Paint.Align.CENTER
        textSize = 72f
        setShadowLayer(12f, 0f, 0f, 0xFFB388FF.toInt())
    }
    private val freqPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFB388FF.toInt()
        textAlign = Paint.Align.CENTER
        textSize = 26f
        setShadowLayer(8f, 0f, 0f, 0xFFB388FF.toInt())
    }
    private val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xAAFFFFFF.toInt()
        textAlign = Paint.Align.CENTER
        textSize = 22f
    }

    private var running = false
    private val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
    private val ampmFormat = SimpleDateFormat("a", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())

    private val ticker = object : Runnable {
        override fun run() {
            invalidate()
            if (running) postDelayed(this, 1000)
        }
    }

    fun start() {
        running = true
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        post(ticker)
    }

    fun stop() {
        running = false
        removeCallbacks(ticker)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val freqIndex = hour % 7
        val freq = solfeggioFrequencies[freqIndex]

        val timeStr = timeFormat.format(cal.time)
        val ampm = ampmFormat.format(cal.time)
        val dateStr = dateFormat.format(cal.time)

        val centerY = height / 2f

        // Date
        canvas.drawText(dateStr, cx, centerY - timePaint.textSize * 0.8f, datePaint)

        // Time
        canvas.drawText("$timeStr $ampm", cx, centerY + timePaint.textSize * 0.3f, timePaint)

        // Frequency
        canvas.drawText(freq, cx, centerY + timePaint.textSize * 1.1f, freqPaint)
    }
}
