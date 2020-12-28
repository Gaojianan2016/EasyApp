package com.gjn.easyapp

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

class CenterAlignImageSpan(drawable: Drawable) : ImageSpan(drawable) {

    override fun draw(
        canvas: Canvas, text: CharSequence?,
        start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int,
        paint: Paint
    ) {
        //计算y方向的位移
        val transY = (y + paint.fontMetrics.descent
                + y + paint.fontMetrics.ascent) / 2 - drawable.bounds.bottom / 2
        canvas.save()
        //绘制图片位移一段距离
        canvas.translate(x, transY)
        drawable.draw(canvas)
        canvas.restore()
    }
}