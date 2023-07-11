package com.abelflynn.pushups

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class ScaleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var progress = 0f
    private val paint = Paint()

    init {
        paint.color = Color.GRAY
    }

    fun getProgress() = progress

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bottom = height.toFloat()
        val top = bottom - (height * progress)
        canvas.drawRect(0f, top, width.toFloat(), bottom, paint)
    }

}

