package com.cec.launcher

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class MandalaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var rotationAngle = 0f
    private var accentColor = 0xFFFFD700.toInt()
    private var running = false
    private val animator = object : Runnable {
        override fun run() {
            rotationAngle = (rotationAngle + 0.3f) % 360f
            invalidate()
            if (running) postDelayed(this, 16)
        }
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.5f
        alpha = 180
    }
    private val starPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 1.2f
        alpha = 140
    }
    private val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        alpha = 60
    }

    fun setAccentColor(color: Int) {
        accentColor = color
        circlePaint.color = color
        starPaint.color = color
        centerPaint.color = color
        circlePaint.setShadowLayer(8f, 0f, 0f, color)
        starPaint.setShadowLayer(6f, 0f, 0f, color)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        invalidate()
    }

    fun resume() {
        running = true
        post(animator)
    }

    fun pause() {
        running = false
        removeCallbacks(animator)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        resume()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pause()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val maxR = min(width, height) / 2f * 0.85f

        canvas.save()
        canvas.translate(cx, cy)

        // Seed of Life - 6 surrounding circles
        canvas.save()
        canvas.rotate(rotationAngle)
        val seedR = maxR * 0.35f
        canvas.drawCircle(0f, 0f, seedR, circlePaint)
        for (i in 0 until 6) {
            val angle = Math.toRadians((i * 60).toDouble())
            val px = (seedR * cos(angle)).toFloat()
            val py = (seedR * sin(angle)).toFloat()
            canvas.drawCircle(px, py, seedR, circlePaint)
        }
        canvas.restore()

        // Flower of Life outer ring
        canvas.save()
        canvas.rotate(-rotationAngle * 0.5f)
        val flowerR = maxR * 0.35f
        val flowerOrbit = maxR * 0.7f
        for (i in 0 until 12) {
            val angle = Math.toRadians((i * 30).toDouble())
            val px = (flowerOrbit * cos(angle)).toFloat()
            val py = (flowerOrbit * sin(angle)).toFloat()
            canvas.drawCircle(px, py, flowerR, circlePaint)
        }
        canvas.restore()

        // 6-pointed star (Star of David)
        canvas.save()
        canvas.rotate(rotationAngle * 0.7f)
        drawStarPolygon(canvas, maxR * 0.55f, 6, starPaint)
        canvas.restore()

        // 12-pointed star
        canvas.save()
        canvas.rotate(-rotationAngle * 0.4f)
        drawStarPolygon(canvas, maxR * 0.8f, 12, starPaint)
        canvas.restore()

        // Metatron's Cube outer circle
        circlePaint.alpha = 80
        canvas.drawCircle(0f, 0f, maxR, circlePaint)
        circlePaint.alpha = 180

        // Center glow
        centerPaint.alpha = 80
        canvas.drawCircle(0f, 0f, maxR * 0.08f, centerPaint)
        centerPaint.alpha = 40
        canvas.drawCircle(0f, 0f, maxR * 0.15f, centerPaint)

        canvas.restore()
    }

    private fun drawStarPolygon(canvas: Canvas, radius: Float, points: Int, paint: Paint) {
        val path = Path()
        val innerRadius = radius * 0.45f
        val totalPoints = points * 2
        for (i in 0 until totalPoints) {
            val angle = Math.toRadians((i * 360.0 / totalPoints) - 90)
            val r = if (i % 2 == 0) radius else innerRadius
            val x = (r * cos(angle)).toFloat()
            val y = (r * sin(angle)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        canvas.drawPath(path, paint)
    }
}
