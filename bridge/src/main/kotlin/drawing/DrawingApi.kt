package drawing

import graph.Circle
import graph.Line

interface DrawingApi {
    val drawingAreaWidth: Int

    val drawingAreaHeight: Int

    fun drawCircle(circle: Circle)

    fun drawLine(line: Line)

    fun print()
}