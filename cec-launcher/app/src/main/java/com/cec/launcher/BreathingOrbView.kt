package com.cec.launcher

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class BreathingOrbView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var orbColor = 0xFFFFD700.toInt()
    private var currentScale = 0.6f
    private var isInhaling = true
    private var breathAnimator: ValueAnimator? = null

    private val orbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        alpha = 60
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFFFFFF.toInt()
        textAlign = Paint.Align.CENTER
        textSize = 28f
        setShadowLayer(6f, 0f, 0f, 0xFFFFFFFF.toInt())
    }

    private var breathLabel = "Inhale"

    fun setOrbColor(color: Int) {
        orbColor = color
        invalidate()
    }

    fun startBreathing() {
        breathAnimator?.cancel()
        breathAnimator = ValueAnimator.ofFloat(0.55f, 1f).apply {
            duration = 4000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { anim ->
                currentScale = anim.animatedValue as Float
                val fraction = anim.animatedFraction
                isInhaling = anim.repeatCount % 2 == 0
                breathLabel = if (isInhaling) {
                    if (fraction < 0.5f) "Inhale" else "Hold"
                } else {
                    "Exhale"
                }
                invalidate()
            }
        }
        breathAnimator?.start()
    }

    fun resume() {
        if (breathAnimator?.isPaused == true) breathAnimator?.resume()
        else startBreathing()
    }

    fun pause() {
        breathAnimator?.pause()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        breathAnimator?.cancel()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val maxR = minOf(width, height) / 2f * 0.85f
        val orbR = maxR * currentScale

        setLayerType(LAYER_TYPE_SOFTWARE, null)

        // Outer glow
        glowPaint.shader = RadialGradient(
            cx, cy, orbR * 1.6f,
            intArrayOf(orbColor and 0x40FFFFFF.toInt(), 0x00000000),
            floatArrayOf(0f, 1f),
            Shader.TileMode.CLAMP
        )
        canvas.drawCircle(cx, cy, orbR * 1.6f, glowPaint)

        // Core orb with gradient
        orbPaint.shader = RadialGradient(
            cx - orbR * 0.3f, cy - orbR * 0.3f, orbR,
            intArrayOf(
                0xFFFFFFFF.toInt() and (orbColor or 0xFF000000.toInt()),
                orbColor,
                (orbColor and 0x00FFFFFF) or 0x88000000.toInt()
            ),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        canvas.drawCircle(cx, cy, orbR, orbPaint)

        // Breath label
        canvas.drawText(breathLabel, cx, cy + textPaint.textSize / 3f, textPaint)
    }
}
