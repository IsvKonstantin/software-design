package graph

import drawing.DrawingApi
import java.lang.Math.PI
import kotlin.math.cos
import kotlin.math.sin

abstract class Graph(private val drawingApi: DrawingApi) {
    private val points = mutableMapOf<Int, Point>()

    abstract fun draw()

    abstract fun readFromFile(filename: String)

    protected fun drawVertices(verticesCount: Int) {
        val widthWithOffset = drawingApi.drawingAreaWidth / 2
        val heightWithOffset = drawingApi.drawingAreaHeight / 2
        val step = 2 * PI / verticesCount
        val alpha = 200
        val radius = 25.0

        (0 until verticesCount).forEach {
            val x = (alpha * cos(2 * PI - it * step) + widthWithOffset)
            val y = (alpha * sin(2 * PI - it * step) + heightWithOffset)
            points[it] = Point(x + radius / 2, y + radius / 2)

            drawingApi.drawCircle(Circle(Point(x, y), radius))
        }
    }

    protected fun drawEdge(from: Int, to: Int) {
        val fromPoint = points.getValue(from)
        val toPoint = points.getValue(to)
        drawingApi.drawLine(Line(fromPoint, toPoint))
    }
}