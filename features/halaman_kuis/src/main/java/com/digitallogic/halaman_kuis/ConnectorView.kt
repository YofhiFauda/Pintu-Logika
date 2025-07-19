package com.digitallogic.halaman_kuis

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class ConnectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    private val lines = mutableListOf<Line>()
    private val path = Path()

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.MITER // Lebih tegas di siku
    }

    fun setLines(linesToDraw: List<Line>) {
        lines.clear()
        lines.addAll(linesToDraw)
        invalidate()
    }

    fun clearLines() {
        lines.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        lines.forEach { line ->
            linePaint.color = line.color
            drawLineWithCorners(canvas, line)
        }
    }

    private fun drawLineWithCorners(canvas: Canvas, line: Line) {
        path.reset()
        path.moveTo(line.start.x, line.start.y)

        // Garis siku: start → middle1 → middle2 → end
        path.lineTo(line.middle1.x, line.middle1.y)
        path.lineTo(line.middle2.x, line.middle2.y)
        path.lineTo(line.end.x, line.end.y)

        canvas.drawPath(path, linePaint)
    }



    data class Line(
        val start: PointF,
        val middle1: PointF,
        val middle2: PointF,
        val end: PointF,
        val color: Int
    )
}
