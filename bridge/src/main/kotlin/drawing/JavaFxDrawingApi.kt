package drawing

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import javafx.stage.Stage
import graph.Circle as MyCircle
import graph.Line as MyLine

class JavaFxDrawingApi : DrawingApi, Application() {
    companion object {
        private val shapes: MutableList<Shape> = ArrayList()
    }

    override val drawingAreaWidth = 800

    override val drawingAreaHeight = 600

    override fun drawCircle(circle: MyCircle) {
        shapes.add(Circle(circle.center.x, circle.center.y, circle.radius))
    }

    override fun drawLine(line: MyLine) {
        shapes.add(Line(line.from.x, line.from.y, line.to.x, line.to.y))
    }

    override fun show() = launch()

    override fun start(stage: Stage?) {
        stage?.run {
            val root: Group = Group().apply { this.children.addAll(shapes) }
            scene = Scene(root, drawingAreaWidth.toDouble(), drawingAreaHeight.toDouble())
            show()
        }
    }
}