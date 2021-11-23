package graph

import drawing.DrawingApi
import java.lang.Math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

abstract class Graph(private val drawingApi: DrawingApi) {
    private val points = mutableMapOf<Int, Point>()

    abstract fun draw()

    abstract fun readFromFile(filename: String)

    protected fun drawVertices(verticesCount: Int) {
        val widthWithOffset = drawingApi.drawingAreaWidth / 2.0
        val heightWithOffset = drawingApi.drawingAreaHeight / 2.0
        val alpha = min(widthWithOffset, heightWithOffset) * 2.0 / 3.0
        val radius = alpha / 10.0

        (0 until verticesCount).forEach {
            val x = widthWithOffset + alpha * cos(2.0 * PI * (1.0 - it / verticesCount.toDouble()))
            val y = heightWithOffset + alpha * sin(2.0 * PI * (1.0 - it / verticesCount.toDouble()))
            points[it] = Point(x, y)

            drawingApi.drawCircle(Circle(Point(x, y), radius))
        }
    }

    protected fun drawEdge(from: Int, to: Int) {
        val fromPoint = points.getValue(from)
        val toPoint = points.getValue(to)
        drawingApi.drawLine(Line(fromPoint, toPoint))
    }
}